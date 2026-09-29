package com.wishINC.bdayboard

import android.app.Application
import com.wishINC.bdayboard.data.BirthdayDatabase

class BdayBoardApplication : Application() {
    val database by lazy { BirthdayDatabase.getDatabase(this) }
}
