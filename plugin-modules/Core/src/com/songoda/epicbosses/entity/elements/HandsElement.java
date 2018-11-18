package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class HandsElement {

    @Expose @Getter @Setter private String mainHand, offHand;

    public HandsElement(String mainHand, String offHand) {
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

}
