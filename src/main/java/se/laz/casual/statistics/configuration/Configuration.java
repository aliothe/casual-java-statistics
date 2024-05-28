/*
 * Copyright (c) 2024, The casual project. All rights reserved.
 *
 * This software is licensed under the MIT license, https://opensource.org/licenses/MIT
 */

package se.laz.casual.statistics.configuration;

import se.laz.casual.statistics.pool.Host;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record Configuration(List<Host> hosts)
{
    public Configuration
    {
        Objects.requireNonNull(hosts, "hosts cannot be null");
    }
    public List<Host> hosts()
    {
        return Collections.unmodifiableList(hosts);
    }
}
