package com.hyuuny.norja.address.domain

import javax.persistence.Embeddable

@Embeddable
class Address(
    var zipcode: String,
    var address: String,
    var detailAddress: String,
) {
}