/*
 * Copyright 2023, Benjamin Mwalimu Mulyungi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rekast.momoapi.model

import com.google.gson.annotations.SerializedName
import com.rekast.momoapi.utils.TransactionType

/**
 *Data class for Payment Body.
 */
class LNMExpress {

    @SerializedName("BusinessShortCode")
    private var businessShortCode: String
    private lateinit var passKey: String

    @SerializedName("Password")
    private lateinit var password: String

    @SerializedName("Timestamp")
    private lateinit var timestamp: String
    private lateinit var type: TransactionType

    @SerializedName("Amount")
    private var amount: String

    @SerializedName("TransactionType")
    private lateinit var transactionType: String

    @SerializedName("PartyA")
    private var partyA: String

    @SerializedName("PartyB")
    private var partyB: String

    @SerializedName("PhoneNumber")
    private var phoneNumber: String

    @SerializedName("CallBackURL")
    private var callBackURL: String

    @SerializedName("AccountReference")
    private var accountReference: String

    @SerializedName("TransactionDesc")
    private var transactionDesc: String

    constructor(
        businessShortCode: String,
        password: String,
        timestamp: String,
        amount: String,
        transactionType: String,
        partyA: String,
        partyB: String,
        phoneNumber: String,
        callBackURL: String,
        accountReference: String,
        transactionDesc: String,
    ) {
        this.businessShortCode = businessShortCode
        this.password = password
        this.timestamp = timestamp
        this.transactionType = transactionType
        this.amount = amount
        this.partyA = partyA
        this.partyB = partyB
        this.phoneNumber = phoneNumber
        this.callBackURL = callBackURL
        this.accountReference = accountReference
        this.transactionDesc = transactionDesc
    }

    constructor(
        businessShortCode: String,
        passKey: String,
        transactionType: TransactionType,
        amount: String,
        partyA: String,
        partyB: String,
        phoneNumber: String,
        callBackURL: String,
        accountReference: String,
        transactionDesc: String,
    ) {
        this.businessShortCode = businessShortCode
        this.passKey = passKey
        type = transactionType
        this.amount = amount
        this.partyA = partyA
        this.partyB = partyB
        this.phoneNumber = phoneNumber
        this.callBackURL = callBackURL
        this.accountReference = accountReference
        this.transactionDesc = transactionDesc
    }
}
