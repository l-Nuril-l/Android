using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Models
{
    public class Car
    {
        public int CarId { get; set; }
        [Required]
        public string Title { get; set; }
        [Required]
        public string Model { get; set; }
        [Required]
        public int Price { get; set; }
    }
}
