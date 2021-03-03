/*
 * Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.siddhi.extension.map.binarypassthrough.sinkmapper;

import io.siddhi.annotation.Example;
import io.siddhi.annotation.Extension;
import io.siddhi.core.config.SiddhiAppContext;
import io.siddhi.core.event.Event;
import io.siddhi.core.stream.output.sink.SinkListener;
import io.siddhi.core.stream.output.sink.SinkMapper;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.core.util.transport.TemplateBuilder;
import io.siddhi.query.api.definition.StreamDefinition;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * This is a sample class-level comment, explaining what the extension class does.
 */

/**
 * Annotation of Siddhi Extension.
 * <pre><code>
 * eg:-
 * {@literal @}Extension(
 * name = "The name of the extension",
 * namespace = "The namespace of the extension",
 * description = "The description of the extension (optional).",
 * //Sink Mapper configurations
 * parameters = {
 * {@literal @}Parameter(name = "The name of the first parameter",
 *                               description= "The description of the first parameter",
 *                               type =  "Supported parameter types.
 *                                        eg:{DataType.STRING, DataType.INT, DataType.LONG etc}",
 *                               dynamic= "false
 *                                         (if parameter doesn't depend on each event then dynamic parameter is false.
 *                                         In Source, only use static parameter)",
 *                               optional= "true/false, defaultValue= if it is optional then assign a default value
 *                                          according to the type."),
 * {@literal @}Parameter(name = "The name of the second parameter",
 *                               description= "The description of the second parameter",
 *                               type =   "Supported parameter types.
 *                                         eg:{DataType.STRING, DataType.INT, DataType.LONG etc}",
 *                               dynamic= "false
 *                                         (if parameter doesn't depend on each event then dynamic parameter is false.
 *                                         In Source, only use static parameter)",
 *                               optional= "true/false, defaultValue= if it is optional then assign a default value
 *                                         according to the type."),
 * },
 * //If Sink Mapper system configurations will need then
 * systemParameters = {
 * {@literal @}SystemParameter(name = "The name of the first  system parameter",
 *                                      description="The description of the first system parameter." ,
 *                                      defaultValue = "the default value of the system parameter.",
 *                                      possibleParameter="the possible value of the system parameter.",
 *                               ),
 * },
 * examples = {
 * {@literal @}Example(syntax = "sample query with Sink Mapper annotation that explain how extension use in Siddhi."
 *                              description =" The description of the given example's query."
 *                      ),
 * }
 *
 * )
 * </code></pre>
 */
@Extension(
        name = "binaryPassThrough",
        namespace = "sinkMapper",
        description = " ",
        examples = {
                @Example(
                        syntax = "@sink(type='websocket-server', host='localhost', port='8080', @map" +
                                "(type='binaryPassThrough')) \n " +
                                "define stream BarStream (buffer object);",
                        description = " "
                )
        }
)
// for more information refer https://siddhi.io/en/v5.0/docs/query-guide/#sink-mapper
public class BinaryPassThroughSinkMapper extends SinkMapper {

    /**
     * The initialization method for {@link SinkMapper}, which will be called before other methods and validate
     * the all configuration and getting the initial values.
     *
     * @param streamDefinition containing stream definition bind to the {@link SinkMapper}
     * @param optionHolder     Option holder containing static and dynamic configuration related
     *                         to the {@link SinkMapper}
     * @param map              Unmapped payload for reference
     * @param configReader     to read the sink related system configuration.
     * @param siddhiAppContext the context of the {@link io.siddhi.query.api.SiddhiApp} used to
     *                         get siddhi related utilty functions.
     */
    @Override
    public void init(StreamDefinition streamDefinition, OptionHolder optionHolder, Map<String, TemplateBuilder> map,
                     ConfigReader configReader, SiddhiAppContext siddhiAppContext) {

    }

    /**
     * Returns a list of supported dynamic options (that means for each event value of the option can change) by
     * the transport.
     *
     * @return the list of supported dynamic option keys
     */
    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    /**
     * Returns the list of classes which this sink can consume.
     * Based on the type of the sink, it may be limited to being able to publish specific type of classes.
     * For example, a {@link SinkMapper} of type event can convert to CSV file objects of type String or byte.
     *
     * @return array of supported classes , if extension can support of any types of classes then return empty array .
     */
    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{byte[].class, ByteBuffer.class};
    }

    /**
     * Method to map the events and send them to {@link SinkListener} for publishing.
     *
     * @param events       {@link Event}s that need to be mapped
     * @param optionHolder Option holder containing static and dynamic options related to the mapper
     * @param map          To build the message payload based on the given template
     * @param sinkListener {@link SinkListener} that will be called with the mapped events
     */
    @Override
    public void mapAndSend(Event[] events, OptionHolder optionHolder, Map<String, TemplateBuilder> map,
                           SinkListener sinkListener) {
        Event event = events[0];
        if (event.getData() != null && event.getData().length != 0) {
            ByteBuffer buffer = ByteBuffer.wrap((byte[]) event.getData()[0]);
            sinkListener.publish(buffer);
        }
    }

    /**
     * Method to map the event and send it to {@link SinkListener} for publishing.
     *
     * @param event        {@link Event} that need to be mapped
     * @param optionHolder Option holder containing static and dynamic options related to the mapper
     * @param map          To build the message payload based on the given template
     * @param sinkListener {@link SinkListener} that will be called with the mapped event
     */
    @Override
    public void mapAndSend(Event event, OptionHolder optionHolder, Map<String, TemplateBuilder> map,
                           SinkListener sinkListener) {
        mapAndSend(new Event[]{event}, optionHolder, map, sinkListener);
    }
}
