package com.example.pickapp


class Rides{
     // Rides Model
     var role:String?=null
     var name:String?=null
     var sourceLocation:String?=null
     var destinationLocation:String?=null
     var sourceTime:String?=null
     var destinationTime:String?=null
     var cost:String?=null
     var passenger:String?=null
     var phoneNumber:String?=null
        constructor(){}

     constructor(role:String?,name:String?,sourceLocation:String?,destinationLocation:String?,sourceTime:String?,passenger:String?,phoneNumber:String?
                 ){
         this.name = name
         this.role = role
         this.sourceLocation = sourceLocation
         this.destinationLocation= destinationLocation
         this.sourceTime = sourceTime
         this.passenger = passenger
         this.phoneNumber = phoneNumber
     }

 }



