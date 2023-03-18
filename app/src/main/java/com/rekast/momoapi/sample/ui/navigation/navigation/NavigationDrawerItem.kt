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
package com.rekast.momoapi.sample.ui.navigation.navigation

import com.rekast.momoapi.sample.utils.Constants

sealed class NavigationDrawerItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationDrawerItem(
        Constants.NavigationRoutes.HOME,
        0,
        Constants.NavigationTitle.HOME,
    )

    object Remittance : NavigationDrawerItem(
        Constants.NavigationRoutes.REMITTANCE,
        0,
        Constants.NavigationTitle.REMITTANCE,
    )

    object Collection : NavigationDrawerItem(
        Constants.NavigationRoutes.COLLECTION,
        0,
        Constants.NavigationTitle.COLLECTION,
    )

    object Disbursement : NavigationDrawerItem(
        Constants.NavigationRoutes.DISBURSEMENT,
        0,
        Constants.NavigationTitle.DISBURSEMENT,
    )
}