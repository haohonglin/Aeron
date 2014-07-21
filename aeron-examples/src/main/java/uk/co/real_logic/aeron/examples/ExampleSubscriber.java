/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.aeron.examples;

import uk.co.real_logic.aeron.Aeron;
import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.aeron.driver.MediaDriver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Example Aeron subscriber application
 */
public class ExampleSubscriber
{
    public static final int CHANNEL_ID = ExampleConfiguration.CHANNEL_ID;
    public static final String DESTINATION = ExampleConfiguration.DESTINATION;
    public static final int FRAME_COUNT_LIMIT = ExampleConfiguration.FRAME_COUNT_LIMIT;

    public static void main(final String[] args)
    {
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        final Aeron.ClientContext aeronContext = new Aeron.ClientContext();

        try (final MediaDriver driver = ExampleUtil.createEmbeddedMediaDriver();
             final Aeron aeron = ExampleUtil.createAeron(aeronContext, executor))
        {
            System.out.println("Subscribing to " + DESTINATION + " on channel Id " + CHANNEL_ID);

            // subscription for channel Id 1
            final Subscription subscription = aeron.addSubscription(DESTINATION, CHANNEL_ID,
                    ExampleUtil.printStringMessage(CHANNEL_ID));

            // run the subscriber thread from here
            ExampleUtil.subscriberLoop(FRAME_COUNT_LIMIT).accept(subscription);
        }
        catch (final Exception ex)
        {
            ex.printStackTrace();
        }

        executor.shutdown();
    }
}
