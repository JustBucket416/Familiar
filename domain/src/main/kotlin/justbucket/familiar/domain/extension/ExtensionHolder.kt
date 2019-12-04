package justbucket.familiar.domain.extension

import justbucket.familiar.content.extension.ExtensionConfigurator
import justbucket.familiar.content.extension.ExtensionLocator
import justbucket.familiar.content.extension.ExtensionModelCreator

/**
 * @author JustBucket on 2019-07-22
 */
data class ExtensionHolder(
        val configurator: ExtensionConfigurator,
        val creator: ExtensionModelCreator,
        val locator: ExtensionLocator
)