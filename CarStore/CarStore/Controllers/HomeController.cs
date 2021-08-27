using CarStore.Models;
using CarStore.Models.ViewModels;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
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

        //public IActionResult Index(SortType sortType = SortType.TitleAsc)
        //{
        //    IEnumerable<Car> result = null;
        //    switch (sortType)
        //    {
        //        case SortType.TitleAsc:
        //            result = db.Cars.OrderBy(x => x.Title).ToList();
        //            break;
        //        case SortType.ModelAsc:
        //            result = db.Cars.OrderBy(x => x.Model).ToList();
        //            break;
        //        case SortType.PriceAsc:
        //            result = db.Cars.OrderBy(x => x.Price).ToList();
        //            break;
        //        default:
        //            result = db.Cars.ToList();
        //            break;
        //    }
        //    return View(result);
        //}

        public IActionResult Index()
        {
          
            return View();
        }
      

        public IActionResult CarsList(string search = "", string company = "all")
        {
            var selectListItems = new List<string>
            {
                "all"
            };
            selectListItems.AddRange(db.Cars.Select(x => x.Title));


            var cars = company == "all" ? db.Cars.ToList() : db.Cars.Where(x => x.Title.ToLower() == company.ToLower()).ToList();
            if (!string.IsNullOrEmpty(search))
            {
                cars = cars.Where(x => x.Title.ToLower().Contains(search.ToLower())).ToList();
            }

            return PartialView(new CarListViewModel
            {
                Cars = cars,
                Companies = new SelectList(selectListItems)
            });
        }

        //public IActionResult CarsJson()
        //{
        //    return Json();
        //}

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
        public IActionResult Buy(Order order)
        {
            if (order.Name?.ToLower() == "test")
            {
                ModelState.AddModelError("Name", "You cannot use test");
            }

            if (ModelState.IsValid)
            {
                db.Orders.Add(order);
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            else
            {
                return View();
            }
        }


    }
}
