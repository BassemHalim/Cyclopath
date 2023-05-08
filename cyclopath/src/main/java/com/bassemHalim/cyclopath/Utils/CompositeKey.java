package com.bassemHalim.cyclopath.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class CompositeKey implements Comparable<CompositeKey>, Serializable {


    private String prefix;
    private String postfix;

    @Override
    public String toString() {
        return prefix + postfix;
    }

    @Override
    public int compareTo(CompositeKey compositeKey) {
        return this.toString().compareTo(compositeKey.toString());
    }
}