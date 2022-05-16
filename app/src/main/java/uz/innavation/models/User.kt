package uz.innavation.models

class User {

    var name: String? = null
    var lastName: String? = null
    var fatherName: String? = null
    var phoneNumber: String? = null
    var region: String? = null
    var country: String? = null
    var home: String? = null
    var zipCode: String? = null

    constructor()
    constructor(
        name: String?,
        lastName: String?,
        fatherName: String?,
        phoneNumber: String?,
        region: String?,
        country: String?,
        home: String?,
        zipCode: String?
    ) {
        this.name = name
        this.lastName = lastName
        this.fatherName = fatherName
        this.phoneNumber = phoneNumber
        this.region = region
        this.country = country
        this.home = home
        this.zipCode = zipCode
    }


}