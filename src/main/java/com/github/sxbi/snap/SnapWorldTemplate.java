package com.github.sxbi.snap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@Accessors(fluent = true)
public class SnapWorldTemplate {

    /**
     * The name of the world template.
     * <p>
     * This world must already exist and be created using the following command:
     * <pre>/swm create &lt;templateName&gt; &lt;file|mongodb&gt;</pre>
     */
    private String templateName;

    /**
     * Specifies the loader used to fetch the template.
     * <ul>
     *     <li><b>FILE</b> - Loads the template from local files.</li>
     *     <li><b>MONGODB</b> - Loads the template from a MongoDB database.</li>
     * </ul>
     */
    private SnapWorldLoader snapWorldLoader;

    /**
     * Determines whether the cloned world should be saved.
     * <p>
     * If {@code false}, the world will not persist after a server restart—ideal for temporary or throwaway instances.
     *
     * @Deprecated currently not implemented
     */
    @Deprecated
    private boolean save;

    /**
     * Creates a new SnapWorldTemplate with the given name and loader.
     * <p>
     * The world will not be saved by default.
     *
     * @param templateName     the name of the template world
     * @param snapWorldLoader  the loader used to load the template
     * @return a new SnapWorldTemplate instance
     */
    public static SnapWorldTemplate as(String templateName, SnapWorldLoader snapWorldLoader) {
        return new SnapWorldTemplate(templateName, snapWorldLoader, false);
    }

    /**
     * Creates a new SnapWorldTemplate with the given name, loader, and save option.
     *
     * @param templateName     the name of the template world
     * @param snapWorldLoader  the loader used to load the template
     * @param save             whether the cloned world should be saved after server shutdown
     * @return a new SnapWorldTemplate instance
     */
    public static SnapWorldTemplate as(String templateName, SnapWorldLoader snapWorldLoader, boolean save) {
        return new SnapWorldTemplate(templateName, snapWorldLoader, save);
    }
}
