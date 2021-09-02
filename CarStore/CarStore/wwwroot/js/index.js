var tokenKey = "accessToken"

async function getCars() {

    const token = sessionStorage.getItem(tokenKey)

    const response = await fetch('/api/cars', {
        method: 'GET',
        headers: {
            'Authorization': 'bearer ' + token
        }
    })

    if (response.ok === true) {
        const cars = await response.json()
        let rows = document.querySelector('tbody')
        cars.forEach(car => rows.append(createTableRow(car)))
    }
}


function createTableRow(car) {
    const tr = document.createElement('tr')

    const titleTd = document.createElement('td')
    titleTd.append(car.title)
    tr.append(titleTd)
    const modelTd = document.createElement('td')
    modelTd.append(car.model)
    tr.append(modelTd)
    const priceTd = document.createElement('td')
    priceTd.append(car.price)
    tr.append(priceTd)

    return tr
}

async function createCar(title, model, price) {
    const response = await fetch('api/cars', {
        method: 'POST',
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        body: JSON.stringify({
            title,
            model,
            price: parseInt(price)
        })
    })

    if (response.ok === true) {
        const car = await response.json()
        document.querySelector('tbody').append(createTableRow(car))
    } else {
        const errorData = await response.json()
        console.log(errorData)
        console.log(errorData.errors)
        if (errorData) {
            if (errorData.errors) {

                if (errorData.errors["Title"]) {
                    showError(errorData.errors["Title"])
                }

                if (errorData.errors["Price"]) {
                    showError(errorData.errors["Price"])
                }
               
            }

            if (errorData["Title"]) {
                showError(errorData["Title"])
                console.log(errorData["Title"])
            }

            if (errorData["Price"]) {
                showError(errorData["Price"])
                console.log(errorData["Title"])
            }


            document.getElementById('errors').style.display = 'block'
        }
    }
}

function showError(errors) {
    errors.forEach(error => {
        const p = document.createElement('p')
        p.append(error)
        document.getElementById('errors').append(p)
    })
}


document.forms['carForm'].addEventListener('submit', function (e) {
    e.preventDefault()
    const form = document.forms['carForm']
    const title = form.elements['title'].value
    const model = form.elements['model'].value
    const price = form.elements['price'].value

    createCar(title, model, price)

})

getCars()




async function getTokenAsync() {
    const credentials = {
        login: document.querySelector('#login').value,
        password: document.querySelector('#password').value
    }
   
    const response = await fetch('api/account/token', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    })

    const data = await response.json()
    if (response.ok === true) {
        sessionStorage.setItem(tokenKey, data.access_token)
        getCars()
    } else {
        console.log(response.status, data.errorText)
    }


}


document.getElementById('submitLogin').addEventListener('click', function () {
    getTokenAsync()
    alert('ff')
})