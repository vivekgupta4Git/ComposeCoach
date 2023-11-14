package com.vivekgupta.composecoachmark.coachmark

/**
 *@author Vivek Gupta on 13-11-23
 */


internal fun Map<Int, CoachData>.hasNextValue(dropCount : Int)= values.drop(dropCount).iterator().hasNext()
internal fun Map<Int, CoachData>.nextKey(dropCount: Int) = keys.drop(dropCount).iterator().next()
internal fun Map<Int, CoachData>.firstKey(dropCount : Int) = keys.drop(dropCount).first()
internal fun Map<Int, CoachData>.nextValue(dropCount: Int) = values.drop(dropCount).iterator().next()