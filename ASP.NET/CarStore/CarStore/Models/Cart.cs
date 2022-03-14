using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CarStore.Models
{
    public class Cart
    {
        private List<CartLine> lineCollection = new List<CartLine>();
        public void AddItem(Car car, int quantity)
        {
            CartLine line =
                lineCollection
                .Where(x => x.Car.CarId == car.CarId)
                .FirstOrDefault();
            if (line == null)
            {
                lineCollection.Add(new CartLine
                {
                    Car = car,
                    Quantity = quantity
                });
            }
            else
            {
                line.Quantity += quantity;
            }
        }
        public void RemoveLine(Car car)
        {
            lineCollection.RemoveAll(x => x.Car.CarId == car.CarId);
        }
        public void Clear()
        {
            lineCollection.Clear();
        }
        public decimal ComputeTotalValue()
        {
            return lineCollection.Sum(x => x.Car.Price * x.Quantity);
        }

        public IEnumerable<CartLine> Lines
        {
            get => lineCollection;
            set
            {
                lineCollection = (List<CartLine>)value;
            }
        }
    }

    public class CartLine
    {
        public Car Car { get; set; }
        public int Quantity { get; set; }
    }
}
