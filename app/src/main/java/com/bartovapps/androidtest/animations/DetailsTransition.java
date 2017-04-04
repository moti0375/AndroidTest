package com.bartovapps.androidtest.animations;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * Created by motibartov on 03/04/2017.
 */

public class DetailsTransition extends TransitionSet {

    public DetailsTransition(){
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform());
    }
}
