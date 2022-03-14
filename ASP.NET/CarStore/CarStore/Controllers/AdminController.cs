using CarStore.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Controllers
{
    //[AllowAnonymous]
    [Authorize(Roles="admin")]
    public class AdminController : Controller
    {
        CarContext context;
        public AdminController(CarContext context)
        {
            this.context = context;
        }
        public IActionResult Index()
        {
            return View(context.Cars.ToList());
        }

        public IActionResult Create(int? id)
        {
            if (id == null)
            {
                return View();
            }
            else
            {
                return View(context.Cars.FirstOrDefault(x => x.CarId == id));
            }
        }

        [HttpPost]
        public IActionResult Create(Car car)
        {
            if (car.CarId == 0)
            {
                context.Cars.Add(car);
            }
            else
            {
                var carEntity = context.Cars.FirstOrDefault(x => x.CarId == car.CarId);
                carEntity.Title = car.Title;
                carEntity.Model = car.Model;
                carEntity.Price = car.Price;
                context.Entry(carEntity).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            }
            context.SaveChanges();

            return RedirectToAction("Index");
        }

        [HttpPost]
        public IActionResult Delete(int carId)
        {
            var carToDelete = context.Cars.Find(carId);
            context.Cars.Remove(carToDelete);
            context.SaveChanges();
            return RedirectToAction("Index");
        }
    }
}
