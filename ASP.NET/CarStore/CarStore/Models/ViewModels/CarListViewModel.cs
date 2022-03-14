using Microsoft.AspNetCore.Mvc.Rendering;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Models.ViewModels
{
    public class CarListViewModel
    {
        public List<Car> Cars { get; set; }
        public SelectList Companies { get; set; }
    }
}
