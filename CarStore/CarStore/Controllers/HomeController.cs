using CarStore.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Controllers
{
    public class HomeController : Controller
    {
        CarContext db;

        public HomeController(CarContext context)
        {
            db = context;
        }

        public IActionResult Index()
        {
            return View(db.Cars.ToList());
        }

        [HttpGet]
        public IActionResult Buy(int? id)
        {
            if (id == null)
            {
                return RedirectToAction("Index");
            }

            ViewBag.CarId = id;
            return View();
        }

        [HttpPost]
        public string Buy(Order order)
        {
            db.Orders.Add(order);
            db.SaveChanges();
            return $"Thanks, {order.Name}!";
        }

      
    }
}
