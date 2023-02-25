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
package com.rekast.momoapi.model.api

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatus(
    @SerializedName("amount") var amount: String,
    @SerializedName("currency") var currency: String,
    @SerializedName("financialTransactionId") var financialTransactionId: String,
    @SerializedName("externalId") var externalId: String,
    @SerializedName("payer") var payer: Payer,
    @SerializedName("payerMessage") var payerMessage: String,
    @SerializedName("payeeNote") var payeeNote: String,
    @SerializedName("status") var status: String,
    @SerializedName("reason") var reason: MomoErrorResponse,
)