package justbucket.familiar.domain.extension

import justbucket.familiar.extension.ExtensionConfigurator
import justbucket.familiar.extension.ExtensionLocator
import justbucket.familiar.extension.ExtensionModelMapper

/**
 * @author JustBucket on 2019-07-22
 */
data class ExtensionHolder(
        val configurator: ExtensionConfigurator,
        val mapper: ExtensionModelMapper,
        val locator: ExtensionLocator
)