import { addProductToDB } from "@/actions/serverActions"
import AddProduct from "@/components/AddProduct"
import { Product } from "@/typings"

export default async function Home() {

  const resp = await fetch ("https://64d88adb5f9bf5b879ce54d0.mockapi.io/products", {
    cache: "no-cache",
    next: {
      tags:["products"]
    }
  })

  const products: Product[] = await resp.json()


  return (
    <main className=''>
      <h1 className="text-3xl font-bold text-center">Product Warehouse</h1>

      <AddProduct/>

      <form action={addProductToDB} className="flex flex-col gap-5 max-w-lg mx-auto p-5">
        <input name="product" type="text" className="border border-gray-300 p-2 rounded-md" placeholder="Enter Product Name..."/>
        <input name="price" type="text" className="border border-gray-300 p-2 rounded-md" placeholder="Enter Price..."/>
        <button className='border bg-blue-500 text-white p-2 rounded-md'>Add Product </button>
      </form>

      <h2 className="font-bold p-5"> List of products</h2>

      <div className="flex flex-wrap gap-5">
      {products?.map(product => (
        <div key={product.id} className='p-5 shadow'>
          <p>{product.product}</p>
          <p>INR {product.price}</p>
        </div>
      ))}
      </div>
    </main>
  )
}
