package com.example.assignment.datamodels

data class Item (
    val id:Int,
    val name:String,
    val parent_id:Int,
    val subCategory:ArrayList<SubCategory>?=null
)