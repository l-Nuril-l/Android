using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Models
{
    public class Car
    {
        public int CarId { get; set; }
        public string Title { get; set; }
        public string Model { get; set; }
        public int Price { get; set; }
    }
}
