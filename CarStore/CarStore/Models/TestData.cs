using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Models
{
    public class TestData
    {
        public static void Initialize(CarContext context)
        {
            if (!context.Cars.Any())
            {
                context.Cars.AddRange(
                    new Car
                    {
                        Title = "Lexus",
                        Model = "RX350",
                        Price = 50000
                    },
                     new Car
                     {
                         Title = "Honda",
                         Model = "Accord",
                         Price = 45000
                     },
                      new Car
                      {
                          Title = "Daewoo",
                          Model = "Lanos",
                          Price = 10000
                      }
               );
                context.SaveChanges();
            }
        }
    }
}
