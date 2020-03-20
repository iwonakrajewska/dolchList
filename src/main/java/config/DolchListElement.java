/*
 * Copyright (c) 2020 Mastercard. All rights reserved.
 */

package config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DolchListElement {

    private String word;
    private String filePath;

}
