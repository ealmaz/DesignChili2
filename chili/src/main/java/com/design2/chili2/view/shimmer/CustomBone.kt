package com.design2.chili2.view.shimmer

import android.view.View
import com.eudycontreras.boneslibrary.framework.bones.BoneBuilder

data class CustomBone(val view: View, val builder: BoneBuilder.() -> Unit)