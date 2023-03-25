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
package com.rekast.momoapi.sample.ui.collection

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rekast.momoapi.MomoAPI
import com.rekast.momoapi.callback.APIResult
import com.rekast.momoapi.model.api.AccountHolder
import com.rekast.momoapi.model.api.MomoTransaction
import com.rekast.momoapi.model.api.Notification
import com.rekast.momoapi.sample.BuildConfig
import com.rekast.momoapi.sample.ui.main.AppMainViewModel
import com.rekast.momoapi.sample.utils.SnackBarComponentConfiguration
import com.rekast.momoapi.sample.utils.Utils
import com.rekast.momoapi.utils.AccountHolderType
import com.rekast.momoapi.utils.Constants
import com.rekast.momoapi.utils.ProductType
import com.rekast.momoapi.utils.Settings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import timber.log.Timber

class CollectionScreenViewModel : ViewModel() {
    private val _snackBarStateFlow = MutableSharedFlow<SnackBarComponentConfiguration>()
    val snackBarStateFlow: SharedFlow<SnackBarComponentConfiguration> = _snackBarStateFlow.asSharedFlow()
    var context: Context? = null
    private var momoAPi: MomoAPI? = null
    private lateinit var _appMainViewModel: AppMainViewModel
    val showProgressBar = MutableLiveData(false)

    private val _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    private val _financialId = MutableLiveData("")
    val financialId: LiveData<String>
        get() = _financialId

    private val _amount = MutableLiveData("")
    val amount: LiveData<String>
        get() = _amount

    private val _payerMessage = MutableLiveData("")
    val paymentMessage: LiveData<String>
        get() = _payerMessage

    private val _payerNote = MutableLiveData("")
    val paymentNote: LiveData<String>
        get() = _payerNote


    fun onPhoneNumberUpdated(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun onFinancialIdUpdated(financialId: String) {
        _financialId.value = financialId
    }

    fun onAmountUpdated(amount: String) {
        _amount.value = amount
    }

    fun onPayerMessageUpdated(payerMessage: String) {
        _payerMessage.value = payerMessage
    }

    fun onPayerNoteUpdated(payerNote: String) {
        _payerNote.value = payerNote
    }

    fun requestToPay() {
        showProgressBar.postValue(true)
        if (phoneNumber.value!!.isNotEmpty() && financialId.value!!.isNotEmpty()
            && amount.value!!.isNotEmpty() && paymentMessage.value!!.isNotEmpty()
            && paymentNote.value!!.isNotEmpty()
        ) {
            val accessToken = context?.let { Utils.getAccessToken(it) }
            val creditTransaction = createRequestTpPayTransaction()
            val transactionUuid = Settings.generateUUID()
            if (StringUtils.isNotBlank(accessToken)) {
                accessToken?.let {
                    momoAPi?.requestToPay(
                        it,
                        creditTransaction,
                        BuildConfig.MOMO_API_VERSION_V1,
                        Settings.getProductSubscriptionKeys(ProductType.COLLECTION),
                        transactionUuid,
                    ) { momoAPIResult ->
                        when (momoAPIResult) {
                            is APIResult.Success -> {
                                showProgressBar.postValue(false)
                                requestToPayDeliveryNotification(transactionUuid, Constants.ProductTypes.COLLECTION)
                                requestToPayTransactionStatus(transactionUuid)
                                _appMainViewModel.refund(transactionUuid)
                            }
                            is APIResult.Failure -> {
                                showProgressBar.postValue(false)
                                val momoAPIException = momoAPIResult.APIException
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createRequestTpPayTransaction(): MomoTransaction {
        return MomoTransaction(
            amount.value!!.toString(),
            "EUR",
            financialId.value!!.toString(),
            Settings.generateUUID(),
            null,
            AccountHolder(AccountHolderType.MSISDN.name, phoneNumber.value!!.toString()),
            paymentMessage.value!!.toString(),
            paymentNote.value!!.toString(),
            null,
            null,
        )
    }

    private fun requestToPayTransactionStatus(referenceId: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                momoAPi?.requestToPayTransactionStatus(
                    referenceId,
                    BuildConfig.MOMO_API_VERSION_V1,
                    Settings.getProductSubscriptionKeys(ProductType.COLLECTION),
                    it
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is APIResult.Success -> {
                            val completeTransfer =
                                Gson().fromJson(momoAPIResult.value!!.source().readUtf8(), MomoTransaction::class.java)
                            Timber.d(completeTransfer.toString())
                        }
                        is APIResult.Failure -> {
                            val momoAPIException = momoAPIResult.APIException
                        }
                    }
                }
            }
        }
    }

    private fun requestToWithdraw() {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        val creditTransaction = createRequestTpPayTransaction()
        val transactionUuid = Settings.generateUUID()
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                momoAPi?.requestToWithdraw(
                    it,
                    creditTransaction,
                    BuildConfig.MOMO_API_VERSION_V2,
                    Settings.getProductSubscriptionKeys(ProductType.COLLECTION),
                    transactionUuid
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is APIResult.Success -> {
                            requestToWithdrawTransactionStatus(transactionUuid)
                        }
                        is APIResult.Failure -> {
                            val momoAPIException = momoAPIResult.APIException
                        }
                    }
                }
            }
        }
    }

    private fun requestToWithdrawTransactionStatus(referenceId: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                momoAPi?.requestToWithdrawTransactionStatus(
                    referenceId,
                    BuildConfig.MOMO_API_VERSION_V1,
                    Settings.getProductSubscriptionKeys(ProductType.COLLECTION),
                    it
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is APIResult.Success -> {
                            val completeTransfer =
                                Gson().fromJson(momoAPIResult.value!!.source().readUtf8(), MomoTransaction::class.java)
                            Timber.d(completeTransfer.toString())
                        }
                        is APIResult.Failure -> {
                            val momoAPIException = momoAPIResult.APIException
                        }
                    }
                }
            }
        }
    }

    private fun requestToPayDeliveryNotification(referenceId: String, productType: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        val notification = Notification(
            notificationMessage =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur pellentesque mi" +
                " erat, vel placerat erat sollicitudin et. Sed id ex nisi. Quisque luctus metus"
        )
        if (StringUtils.isNotBlank(accessToken) &&
            Settings.checkNotificationMessageLength(notification.notificationMessage)
        ) {
            accessToken?.let {
                momoAPi?.requestToPayDeliveryNotification(
                    notification,
                    referenceId,
                    BuildConfig.MOMO_API_VERSION_V1,
                    productType,
                    Settings.getProductSubscriptionKeys(ProductType.COLLECTION),
                    it
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is APIResult.Success -> {
                        }
                        is APIResult.Failure -> {
                            val momoAPIException = momoAPIResult.APIException
                        }
                    }
                }
            }
        }
    }

    fun provideContext(fragmentContext: Context) {
        context = fragmentContext
    }

    fun provideMomoAPI(fragmentMomoAPI: MomoAPI) {
        momoAPi = fragmentMomoAPI
    }

    fun provideAppMainViewModel(appMainViewModel: AppMainViewModel) {
        _appMainViewModel = appMainViewModel
    }

    private fun emitSnackBarState(snackBarComponentConfiguration: SnackBarComponentConfiguration) {
        viewModelScope.launch { _snackBarStateFlow.emit(snackBarComponentConfiguration) }
    }
}
