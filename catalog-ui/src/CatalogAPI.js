
const api = "https://catalog.app.gclouddemo.com/list?p=true"


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



export const getAll = () =>
  fetch(`${api}`, fetchInfo )
    .then(res => {
     // console.log('getAll-1', res.json())
      return [
  {
    "id": 2,
    "sku": "AB-0983-Z",
    "summary": "Google Women\u0027s Short Sleeve Hero Tee Black",
    "description": "100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.",
    "price": 16.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA"
  },
  {
    "id": 3,
    "sku": "AE-1283-M",
    "summary": "Google Women\u0027s Long Sleeve Blended Cardigan Charcoal",
    "description": "Made in the USA quality and a soft-feeling triple blended material make this cardigan a winner in cool weather or cold offices.",
    "price": 20.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0308.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0308-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "Outerwear",
    "details": "38% cotton/50% polyester/12% rayon\nMade in USA"
  },
  {
    "id": 4,
    "sku": "EE-9122-E",
    "summary": "Google Men\u0027s 100% Cotton Short Sleeve Hero Tee White",
    "description": "Be a Google Hero. 100% cotton jersey fabric sets this Google t-shirt above the crowd. Features the Google logo across the chest.",
    "price": 16.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0104.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0104-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles Jersey\nMade in the USA"
  },
  {
    "id": 5,
    "sku": "EF-4454-A",
    "summary": "Android Men\u0027s Engineer Short Sleeve Tee Charcoal",
    "description": "100% cotton fabric and the Android robot logo combines comfort and style to this t-shirt.",
    "price": 19.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEAXXX0808.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEAXXX0808-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles Jersey\nMade in the USA"
  },
  {
    "id": 6,
    "sku": "ZZ-0881-A",
    "summary": "Google Men\u0027s Vintage Badge Tee Black",
    "description": "This Google t-shirt is soft, comfortable, and lets you show your Google affinity with a classic style. 50/50 cotton-polyester blend.",
    "price": 9.49,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0338.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0338-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "4.4 ounce, 50/50 cotton/poly\nTear away label\nRib-bound neck"
  },
  {
    "id": 7,
    "sku": "AL-5565-Z",
    "summary": "Google Tri-blend Hoodie Grey",
    "description": "Tri-blend jersey fabric lends this Google hoodie a distinct appearance. Made in the USA. Decoration on the left chest.",
    "price": 39.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0313.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0313-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "Outerwear",
    "details": "4.5 oz. jersey knit\n50% polyester, 37.5%cotton, 12.5% rayon\n65% polyester/35% cotton sleeves\nMade in the USA"
  },
  {
    "id": 8,
    "sku": "BE-0921-T",
    "summary": "Google Women\u0027s Long Sleeve Tee Lavender",
    "description": "Casual, comfortable, and essentially Google. This Google t-shirt transcends the typical with its arresting hemline and 3/4 sleeves.",
    "price": 12.00,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0363.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0363-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "50% cotton, 50% polyester\nCurved shirttail hem\nGoogle logo decoration"
  },
  {
    "id": 9,
    "sku": "AB-0983-T",
    "summary": "Google Women\u0027s Short Sleeve Hero Tee Black",
    "description": "100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.",
    "price": 16.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA"
  },
  {
    "id": 2,
    "sku": "AB-0983-Z",
    "summary": "Google Women\u0027s Short Sleeve Hero Tee Black",
    "description": "100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.",
    "price": 16.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA"
  },
  {
    "id": 3,
    "sku": "AE-1283-M",
    "summary": "Google Women\u0027s Long Sleeve Blended Cardigan Charcoal",
    "description": "Made in the USA quality and a soft-feeling triple blended material make this cardigan a winner in cool weather or cold offices.",
    "price": 20.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0308.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0308-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "Outerwear",
    "details": "38% cotton/50% polyester/12% rayon\nMade in USA"
  },
  {
    "id": 4,
    "sku": "EE-9122-E",
    "summary": "Google Men\u0027s 100% Cotton Short Sleeve Hero Tee White",
    "description": "Be a Google Hero. 100% cotton jersey fabric sets this Google t-shirt above the crowd. Features the Google logo across the chest.",
    "price": 16.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0104.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0104-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles Jersey\nMade in the USA"
  },
  {
    "id": 5,
    "sku": "EF-4454-A",
    "summary": "Android Men\u0027s Engineer Short Sleeve Tee Charcoal",
    "description": "100% cotton fabric and the Android robot logo combines comfort and style to this t-shirt.",
    "price": 19.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEAXXX0808.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEAXXX0808-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles Jersey\nMade in the USA"
  },
  {
    "id": 6,
    "sku": "ZZ-0881-A",
    "summary": "Google Men\u0027s Vintage Badge Tee Black",
    "description": "This Google t-shirt is soft, comfortable, and lets you show your Google affinity with a classic style. 50/50 cotton-polyester blend.",
    "price": 9.49,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0338.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0338-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "4.4 ounce, 50/50 cotton/poly\nTear away label\nRib-bound neck"
  },
  {
    "id": 7,
    "sku": "AL-5565-Z",
    "summary": "Google Tri-blend Hoodie Grey",
    "description": "Tri-blend jersey fabric lends this Google hoodie a distinct appearance. Made in the USA. Decoration on the left chest.",
    "price": 39.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0313.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0313-th1.jpg",
    "category": "Men\u0027s Apparel",
    "subcategory": "Outerwear",
    "details": "4.5 oz. jersey knit\n50% polyester, 37.5%cotton, 12.5% rayon\n65% polyester/35% cotton sleeves\nMade in the USA"
  },
  {
    "id": 8,
    "sku": "BE-0921-T",
    "summary": "Google Women\u0027s Long Sleeve Tee Lavender",
    "description": "Casual, comfortable, and essentially Google. This Google t-shirt transcends the typical with its arresting hemline and 3/4 sleeves.",
    "price": 12.00,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0363.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0363-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "50% cotton, 50% polyester\nCurved shirttail hem\nGoogle logo decoration"
  },
  {
    "id": 9,
    "sku": "AB-0983-T",
    "summary": "Google Women\u0027s Short Sleeve Hero Tee Black",
    "description": "100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.",
    "price": 16.99,
    "thumb": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278.jpg",
    "image": "https://static.catalog.app.gclouddemo.com/images/GGOEGAAX0278-th1.jpg",
    "category": "Women\u0027s Apparel",
    "subcategory": "T-Shirts",
    "details": "100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA"
  }
]

    })



