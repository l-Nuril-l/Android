async function getCars() {
    const response = await fetch('/api/cars', {
        method: 'GET'
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
    }
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