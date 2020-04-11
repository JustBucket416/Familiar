package justbucket.familiar.utils

/**
 * @author JustBucket on 2019-07-22
 */
class NonNullMap<K, V : Any> : HashMap<K, V>() {

    override operator fun get(key: K): V {
        return super.get(key) ?: throw IllegalArgumentException("Can't find value with key $key")
    }
}