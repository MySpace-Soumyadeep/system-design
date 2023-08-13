"use server"

import { Product } from "@/typings";
import { revalidateTag } from "next/cache";

export const addProductToDB =async (e:FormData) => {
    

    const product = e.get("product")?.toString();
    const price = e.get("price")?.toString();

    if(!product|| !price) return;

    const newProduct: Product = {
      product, price
    }

    await fetch("https://64d88adb5f9bf5b879ce54d0.mockapi.io/products", {
      method: "POST",
      body: JSON.stringify(newProduct),
      headers: {
        "Content-Type": "application/json"
      }
    });

    revalidateTag('products');
  }