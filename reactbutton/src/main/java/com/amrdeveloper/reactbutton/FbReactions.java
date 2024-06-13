package com.amrdeveloper.reactbutton;

import com.example.kosovo.R;

public class FbReactions {

    private static Reaction defaultReact = new Reaction(
            ReactConstants.LIKE,
            ReactConstants.DEFAULT,
            ReactConstants.GRAY,
            R.drawable.ic_gray_like);

    private  static Reaction[] reactions = {
            new Reaction(ReactConstants.LIKE,ReactConstants.BLUE,R.drawable.ic_like),
            new Reaction(ReactConstants.LOVE,ReactConstants.RED_LOVE,R.drawable.ic_heart),
            new Reaction(ReactConstants.CARE,ReactConstants.ORANGE_CARE,R.drawable.ic_care),
            new Reaction(ReactConstants.HAHA,ReactConstants.YELLOW_WOW,R.drawable.ic_happy),
            new Reaction(ReactConstants.WOW,ReactConstants.YELLOW_WOW,R.drawable.ic_surprise),
            new Reaction(ReactConstants.SAD,ReactConstants.YELLOW_HAHA,R.drawable.ic_sad),
            new Reaction(ReactConstants.ANGRY,ReactConstants.RED_ANGRY,R.drawable.ic_angry),
    };


    public static Reaction getDefaultReact() {
        return defaultReact;
    }

    public static Reaction[] getReactions() {
        return reactions;
    }
    public static Reaction getReaction(String reactionType) {
        if(reactionType.contentEquals(ReactConstants.DEFAULT)){
            return getDefaultReact();
        }else if(reactionType.contentEquals(ReactConstants.LIKE)){
            return getReactions()[0];
        }else if(reactionType.contentEquals(ReactConstants.LOVE)){
            return getReactions()[1];
        }else if(reactionType.contentEquals(ReactConstants.CARE)){
            return getReactions()[2];
        }else if(reactionType.contentEquals(ReactConstants.HAHA)){
            return getReactions()[3];
        }else if(reactionType.contentEquals(ReactConstants.WOW)){
            return getReactions()[4];
        }else if(reactionType.contentEquals(ReactConstants.SAD)){
            return getReactions()[5];
        }else if(reactionType.contentEquals(ReactConstants.ANGRY)){
            return getReactions()[6];
        }else{
            return getDefaultReact();
        }
    }
}
