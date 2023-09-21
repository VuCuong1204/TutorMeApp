package vn.tutorme.mobile.base.common.anim

import vn.tutorme.mobile.R

class SlideAnimation(private val type: SLIDE_TYPE = SLIDE_TYPE.RIGHT_TO_LEFT) : IScreenAnim {
    override fun enter() = when (type) {
        SLIDE_TYPE.BOTTOM_TO_TOP -> R.anim.slide_enter_bottom_to_top
        else -> R.anim.slide_enter_left_to_right
    }

    override fun exit() = when (type) {
        SLIDE_TYPE.BOTTOM_TO_TOP -> R.anim.slide_exit_top_to_bottom
        else -> R.anim.slide_exit_right_to_left
    }

    override fun popEnter() = when (type) {
        SLIDE_TYPE.BOTTOM_TO_TOP -> R.anim.slide_pop_enter_top_to_bottom
        else -> R.anim.slide_pop_enter_right_to_left
    }

    override fun popExit() = when (type) {
        SLIDE_TYPE.BOTTOM_TO_TOP -> R.anim.slide_pop_exit_bottom_to_top
        else -> R.anim.slide_pop_exit_left_to_right
    }
}

enum class SLIDE_TYPE {
    RIGHT_TO_LEFT, BOTTOM_TO_TOP
}
