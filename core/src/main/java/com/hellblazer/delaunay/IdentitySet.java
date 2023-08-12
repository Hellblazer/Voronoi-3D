/** 
 * (C) Copyright 2009 Hal Hildebrand, All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.hellblazer.delaunay;

/**
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 * 
 */
public class IdentitySet<T> extends OpenAddressingSet<T> {

    public IdentitySet() {
        super(4);
    }

    public IdentitySet(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    protected boolean equals(Object key, Object ob) {
        return ob == key;
    }

    @Override
    protected int getHash(Object key) {
        return System.identityHashCode(key);
    }
}