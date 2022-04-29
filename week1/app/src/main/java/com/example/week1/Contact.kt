package com.example.week1

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
}