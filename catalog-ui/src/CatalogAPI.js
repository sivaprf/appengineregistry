
const api = "https://catalog.app.gclouddemo.com/list"


// Generate a unique token for storing your bookshelf data on the backend server.
let token = localStorage.token
if (!token)
  token = localStorage.token = Math.random().toString(36).substr(-8)

const fetchInfo = {
  headers : {
    'Accept': 'application/json',
   // 'Authorization': token,
  },
  mode: 'no-cors'
}

// Some items to test dev with; returned only when we can't get through to the
// main REST target for one reason or another. Will disappear when dev is essentially
// complete -- HR.
const testItems = [{
        "id": 2,
        "sku": "AB-0983-Z",
        "summary": "TEST ITEM 1: Google Women\u0027s Short Sleeve Hero Tee Black",
        "description": "100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.",
        "price": 16.99,
        "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278.jpg",
        "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278-th1.jpg",
        "category": "Women\u0027s Apparel",
        "subcategory": "T-Shirts",
        "details": "100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA"
    },
    {
        "id": 4,
        "sku": "EE-9122-E",
        "summary": "TEST ITEM 2: Google Men\u0027s 100% Cotton Short Sleeve Hero Tee White",
        "description": "Be a Google Hero. 100% cotton jersey fabric sets this Google t-shirt above the crowd. Features the Google logo across the chest.",
        "price": 16.99,
        "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0104.jpg",
        "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0104-th1.jpg",
        "category": "Men\u0027s Apparel",
        "subcategory": "T-Shirts",
        "details": "100% Cotton\n30 Singles Jersey\nMade in the USA"
  }]

export const getAll = () =>
  fetch(`${api}`, fetchInfo )
    .then(function(response) {
        if (response.status === 200) {
            return response.json();
        } else {
            return testItems;
        }
    }
    ).catch(function(err) {
        console.log(err);
        return testItems;
    });



