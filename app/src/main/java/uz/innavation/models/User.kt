package uz.innavation.models

class User {

    var name: String? = null
    var lastName: String? = null
    var phoneNumber: String? = null
    var email: String? = null
    var password: String? = null
    var region: String? = null
    var country: String? = null
    var home: String? = null

    constructor()


    constructor(
        name: String?,
        lastName: String?,
        phoneNumber: String?,
        region: String?,
        country: String?,
        home: String?
    ) {
        this.name = name
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.region = region
        this.country = country
        this.home = home
    }

    constructor(
        name: String?,
        lastName: String?,
        phoneNumber: String?,
        email: String?,
        password: String?,
        region: String?,
        country: String?,
        home: String?
    ) {
        this.name = name
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.email = email
        this.password = password
        this.region = region
        this.country = country
        this.home = home
    }


}