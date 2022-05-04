package com.example.week1.Content

// Класс контакта, содержащий имя и номер
class Contact {
    var name : String
    var number : String
    constructor(name: String, number: String) {
        this.name = name
        this.number = number
    }

    override fun toString(): String {
        return "Contact(name='$name', number='$number')"
    }

    @JvmName("getName1")
    fun getName(): String {
        return name
    }

    @JvmName("getNumber1")
    fun getNumber(): String {
        return number
    }
}