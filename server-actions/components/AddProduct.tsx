"use client"

import { addProductToDB } from "@/actions/serverActions";
  import {useTransition} from "react"

function AddProduct() {
  const [isPending, startTransition] = useTransition();

  const formData = new FormData();
  formData.append("product", "Macbook Pro");
  formData.append("price", "380000")

  return (
   <button
   onClick={() => startTransition(() =>  addProductToDB(formData))}
   className="fixed bottom-10 right-10 border bg-green-500 text-white p-2 rounded-md w-48">
    {isPending ? "Adding..." : "Add Macbook Pro"}
   </button>
  )
}

export default AddProduct