import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest

open class License(
    val spdxIdentifier: String,
    val url: String,
) {
    open val urlAliases: Set<String> = emptySet()
    open val compatibleWith: Set<License> = emptySet()


// Permissive Licenses
// ---------------------------------------------------------------------------------------------------------------------

    object Apache : License(
        spdxIdentifier = "Apache-2.0",
        url = "https://opensource.org/licenses/Apache-2.0"
    ) {
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object MIT : License(
        spdxIdentifier = "MIT",
        url = "https://opensource.org/licenses/MIT"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/MIT",
            "http://www.opensource.org/licenses/mit-license.php",
            "http://www.opensource.org/licenses/mit-license.html",
            "https://raw.githubusercontent.com/bit3/jsass/master/LICENSE",
            "https://github.com/mockito/mockito/blob/main/LICENSE",
            "http://json.org/license.html",
            "https://jsoup.org/license",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object BSD2 : License(
        spdxIdentifier = "BSD-2-Clause",
        url = "https://opensource.org/licenses/BSD-2-Clause"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-2-Clause",
            "http://www.opensource.org/licenses/BSD-2-Clause",
            "http://www.opensource.org/licenses/bsd-license.php",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object BSD3 : License(
        spdxIdentifier = "BSD-3-Clause",
        url = "https://opensource.org/licenses/BSD-3-Clause"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-3-Clause",
            "http://www.jcraft.com/jzlib/LICENSE.txt",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object CDDL : License(
        spdxIdentifier = "CDDL-1.0",
        url = "https://opensource.org/licenses/CDDL-1.0"
    ) {
        override val urlAliases = setOf(
            "https://github.com/javaee/javax.annotation/blob/master/LICENSE",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

// Copyleft Licenses
// ---------------------------------------------------------------------------------------------------------------------

    object LGPL_V2 : License(
        spdxIdentifier = "LGPL-2.0",
        url = "https://opensource.org/licenses/LGPL-2.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/lgpl-2.1-standalone.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object LGPL_V3 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/LGPL-3.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/lgpl.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object GPL_V2 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/GPL-2.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/gpl-2.0-standalone.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            GPL_V2, GPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object GPL_V3 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/GPL-3.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/copyleft/gpl.html",
            "http://www.gnu.org/licenses/gpl.txt"
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            GPL_V2, GPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object EPL_V1 : License(
        spdxIdentifier = "EPL-1.0",
        url = "https://opensource.org/licenses/EPL-1.0"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-3-Clause",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object EPL_V2 : License(
        spdxIdentifier = "EPL-2.0",
        url = "https://opensource.org/licenses/EPL-2.0"
    ) {
        override val urlAliases = setOf(
            "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt",
            "https://www.eclipse.org/legal/epl-v20.html",
            "http://www.eclipse.org/legal/epl-v20.html",
            "https://www.eclipse.org/legal/epl-2.0/",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }
}

fun Project.loadProperty(
    projectPropertyName: String,
    envName: String = projectPropertyName.toUpperCase()
): String {
    val envValue = System.getenv(envName)?.toString()
    if (envValue != null) return envValue

    val projectPropertiesValue = project.properties[projectPropertyName]?.toString()
    if (projectPropertiesValue != null) return projectPropertiesValue

    return ""
}

fun Project.loadFileContents(
    projectPropertyName: String,
    envName: String = projectPropertyName.toUpperCase()
): String {
    val decodeIfNeeded: (String) -> String = {
        if (it.startsWith("~/")) {
            // the value is a path to file on disk. Read its contents
            val filePath = it.replace("~/", System.getProperty("user.home") + "/")
            project.file(filePath).readText()
        } else {
            // the value itself is the file contents file
            it
        }
    }

    val envValue = System.getenv(envName)?.toString()
    if (envValue != null) return decodeIfNeeded(envValue)

    val projectPropertiesValue = project.properties[projectPropertyName]?.toString()
    if (projectPropertiesValue != null) return decodeIfNeeded(projectPropertiesValue)

    return ""
}

fun Project.runCommand(command: String): String {
    return runCatching {
        val stdout = ByteArrayOutputStream()

        exec {
            commandLine(*command.split(' ').toTypedArray())
            standardOutput = stdout
        }

        stdout.toString().trim()
    }.getOrElse { "" }
}

// Versioning Utils
// ---------------------------------------------------------------------------------------------------------------------

fun Project.getCurrentSha(): String = runCommand("git rev-parse HEAD")
fun Project.getLatestTagSha(): String = runCommand("git rev-list --tags --max-count=1")
fun Project.getLatestTagName(): String = runCommand("git describe --abbrev=0 --tags")
fun Project.getCommitsSinceLastTag(latestTagName: String): List<String> =
    runCommand("git log ${latestTagName}..HEAD --oneline --pretty=format:%s").lines().reversed()

fun Project.hasUncommittedChanges(): Boolean = runCommand("git status --porcelain").isBlank()

fun String.parseVersion(sha: String): SemanticVersion {
    return this
        .split('.')
        .map { it.trim().toIntOrNull() ?: 0 }
        .let { SemanticVersion(it[0], it[1], it[2], sha) }
}

fun SemanticVersion.bump(
    currentSha: String,
    hasUncommittedChanges: Boolean,
    commitsSinceLastTag: List<String>,
    minorVersionBumpCommitPrefix: String,
    majorVersionBumpCommitPrefix: String,
): SemanticVersion {
    var (_major, _minor, _patch) = this

    if (currentSha != this.sha || hasUncommittedChanges) {
        _patch++
    }

    for (commit in commitsSinceLastTag) {
        if (commit.startsWith(minorVersionBumpCommitPrefix)) {
            _patch = 0
            _minor++
        } else if (commit.startsWith(majorVersionBumpCommitPrefix)) {
            _patch = 0
            _minor = 0
            _major++
        }
    }

    return SemanticVersion(major = _major, minor = _minor, patch = _patch, sha = currentSha)
}

fun ProjectVersion.log() {
    println(
        buildString {
            if (previousVersion == null) {
                appendLine("$projectVersion (${projectSemanticVersion.sha})")
            } else {
                appendLine("${previousVersion.format()} (${previousVersion.sha}) -> $projectVersion (${projectSemanticVersion.sha})")
            }

            for (commit in commitsSincePreviousVersion) {
                appendLine("  - $commit")
            }
        }
    )
}

fun Project.getProjectVersion(
    logChanges: Boolean = false,
    failWithUncommittedChanges: Boolean = false,
    failIfNotRelease: Boolean = false,
    snapshotSuffix: String = "-SNAPSHOT",
    initialVersion: String = "0.1.0",
    majorVersionBumpCommitPrefix: String = "[major]",
    minorVersionBumpCommitPrefix: String = "[minor]"
): ProjectVersion {
    val latestTagName = getLatestTagName()
    val latestTagSha = getLatestTagSha()
    val currentSha = getCurrentSha()
    val commitsSinceLastTag = getCommitsSinceLastTag(latestTagName)
    val isRelease = hasProperty("release")
    val isDocsUpdate = !isRelease && hasProperty("releaseDocs")
    val hasUncommittedChanges = hasUncommittedChanges()

    val isFirstVersion = latestTagName.isBlank()

    val previousVersion: SemanticVersion? = if (isFirstVersion) {
        null
    } else {
        latestTagName.parseVersion(latestTagSha)
    }

    val nextVersion: SemanticVersion = previousVersion
        ?.bump(
            currentSha = currentSha,
            hasUncommittedChanges = hasUncommittedChanges,
            commitsSinceLastTag = commitsSinceLastTag,
            minorVersionBumpCommitPrefix = minorVersionBumpCommitPrefix,
            majorVersionBumpCommitPrefix = majorVersionBumpCommitPrefix,
        )
        ?: initialVersion.parseVersion("")

    // make checks on version
    if (failWithUncommittedChanges) {
        check(!hasUncommittedChanges) { "There are uncommitted changes!" }
    }
    if (failIfNotRelease) {
        check(isRelease) { "This is not a release build!" }
    }

    return ProjectVersion(
        previousVersion = previousVersion,
        nextVersion = nextVersion,
        isRelease = isRelease,
        isDocsUpdate = isDocsUpdate,
        snapshotSuffix = snapshotSuffix,
        latestSha = currentSha,
        commitsSincePreviousVersion = commitsSinceLastTag
    ).also {
        if (logChanges) {
            it.log()
        }
    }
}

data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,

    val sha: String,
) {
    override fun toString(): String {
        return "SemVer(major=$major, minor=$minor, patch=$patch)"
    }

    fun format(
        isSnapshot: Boolean = false,
        snapshotSuffix: String = "",
    ): String {
        return if (isSnapshot) "$major.$minor.$patch$snapshotSuffix" else "$major.$minor.$patch"
    }
}

data class ProjectVersion(
    val previousVersion: SemanticVersion?,
    val nextVersion: SemanticVersion,

    val isRelease: Boolean,
    val isDocsUpdate: Boolean,

    val snapshotSuffix: String,
    val latestSha: String,
    val commitsSincePreviousVersion: List<String>
) {
    val previousOrInitialVersion: SemanticVersion
        get() {
            return previousVersion ?: nextVersion
        }
    val projectSemanticVersion: SemanticVersion
        get() {
            return if (isRelease) {
                nextVersion
            } else if (isDocsUpdate) {
                previousOrInitialVersion
            } else {
                nextVersion
            }
        }
    val projectVersion: String
        get() {
            return if (isRelease) {
                nextVersion.format(false, snapshotSuffix)
            } else if (isDocsUpdate) {
                previousOrInitialVersion.format(false, snapshotSuffix)
            } else {
                nextVersion.format(true, snapshotSuffix)
            }
        }

    val documentationVersion: String
        get() {
            return if (isRelease) {
                // we're releasing a new version of the library, use the newest version
                nextVersion.format(false, snapshotSuffix)
            } else if(isDocsUpdate) {
                // we're publishing an update to the documentation site without releasing an actual update to the library
                previousOrInitialVersion.format(false, snapshotSuffix)
            } else {
                // we're just developing, use the current snapshot version
                nextVersion.format(true, snapshotSuffix)
            }
        }

    override fun toString(): String = projectVersion

    fun debug(): String {
        return """
            |ProjectVersion(
            |    projectVersion=$projectVersion
            |    documentationVersion=$documentationVersion
            |    isRelease=$isDocsUpdate
            |    snapshotSuffix=$snapshotSuffix
            |    latestSha=$latestSha
            |    commitsSincePreviousVersion=${commitsSincePreviousVersion.size}
            |)
        """.trimMargin()
    }
}

data class PublishConfiguration(
    val githubUser: String,
    val githubToken: String,

    val mavenRepositoryBaseUrl: String,
    val stagingRepositoryIdFile: File,
    val stagingProfileId: String,

    val signingKeyId: String,
    val signingKey: String,
    val signingPassword: String,
    val ossrhUsername: String,
    val ossrhPassword: String,

    val jetbrainsMarketplacePassphrase: String,
    val jetbrainsMarketplacePrivateKey: String,
    val jetbrainsMarketplaceCertificateChain: String,
    val jetbrainsMarketplaceToken: String,

    val keystorePassword: String,
    val scriptureMemoryKeyAlias: String,
    val scriptureMemoryKeyPassword: String,
    val openBibleKeyAlias: String,
    val openBibleKeyPassword: String,
) {

    var stagingRepositoryId: String
        get() {
            return if (stagingRepositoryIdFile.exists()) {
                stagingRepositoryIdFile.readText()
            } else {
                ""
            }
        }
        set(value) {
            if (stagingRepositoryIdFile.exists()) {
                stagingRepositoryIdFile.delete()
            }

            stagingRepositoryIdFile.parentFile.mkdirs()
            stagingRepositoryIdFile.createNewFile()

            stagingRepositoryIdFile.writeText(value)
        }

    override fun toString(): String {
        return debug()
    }

    fun debug(): String {
        return """
            |PublishConfiguration(
            |    githubUser=${if (githubUser.isNotBlank()) "[REDACTED]" else ""}
            |    githubToken=${if (githubToken.isNotBlank()) "[REDACTED]" else ""}
            |    mavenRepositoryBaseUrl=$mavenRepositoryBaseUrl
            |    stagingRepositoryIdFile=$stagingRepositoryIdFile
            |    stagingRepositoryId=$stagingRepositoryId
            |    stagingProfileId=$stagingProfileId
            |
            |    signingKeyId=${if (signingKeyId.isNotBlank()) "[REDACTED]" else ""}
            |    signingKey=${if (signingKey.isNotBlank()) "[REDACTED]" else ""}
            |    signingPassword=${if (signingPassword.isNotBlank()) "[REDACTED]" else ""}
            |    ossrhUsername=${if (ossrhUsername.isNotBlank()) "[REDACTED]" else ""}
            |    ossrhPassword=${if (ossrhPassword.isNotBlank()) "[REDACTED]" else ""}
            |    
            |    jetbrainsMarketplacePassphrase=${if (jetbrainsMarketplacePassphrase.isNotBlank()) "[REDACTED]" else ""}
            |    jetbrainsMarketplacePrivateKey=${if (jetbrainsMarketplacePrivateKey.isNotBlank()) "[REDACTED]" else ""}
            |    jetbrainsMarketplaceCertificateChain=${if (jetbrainsMarketplaceCertificateChain.isNotBlank()) "[REDACTED]" else ""}
            |    jetbrainsMarketplaceToken=${if (jetbrainsMarketplaceToken.isNotBlank()) "[REDACTED]" else ""}
            |    
            |    keystorePassword=${if (keystorePassword.isNotBlank()) "[REDACTED]" else ""}
            |    scriptureMemoryKeyAlias=${if (scriptureMemoryKeyAlias.isNotBlank()) "[REDACTED]" else ""}
            |    scriptureMemoryKeyPassword=${if (scriptureMemoryKeyPassword.isNotBlank()) "[REDACTED]" else ""}
            |    openBibleKeyAlias=${if (openBibleKeyAlias.isNotBlank()) "[REDACTED]" else ""}
            |    openBibleKeyPassword=${if (openBibleKeyPassword.isNotBlank()) "[REDACTED]" else ""}
            |)
        """.trimMargin()
    }
}

fun KotlinMultiplatformExtension.nativeTargetGroup(
    name: String,
    vararg targets: KotlinNativeTarget
): Array<out KotlinNativeTarget> {
    sourceSets {
        val (main, test) = if (targets.size > 1) {
            val nativeMain = getByName("commonMain")
            val nativeTest = getByName("commonTest")
            val main = create("${name}Main") {
                dependsOn(nativeMain)
            }
            val test = create("${name}Test") {
                dependsOn(nativeTest)
            }
            main to test
        } else (null to null)

        targets.forEach { target ->
            main?.let {
                target.compilations["main"].defaultSourceSet {
                    dependsOn(main)
                }
            }
            test?.let {
                target.compilations["test"].defaultSourceSet {
                    dependsOn(test)
                }
            }
        }
    }
    return targets
}



object HashUtils {

    const val STREAM_BUFFER_LENGTH = 1024

    fun getCheckSumFromFile(file: File): String {
        val digest = MessageDigest.getInstance(MessageDigestAlgorithm.SHA_1)
        val fis = FileInputStream(file)
        val byteArray = updateDigest(digest, fis).digest()
        fis.close()
        val hexCode = StringUtils.encodeHex(byteArray, true)
        return String(hexCode)
    }

    /**
     * Reads through an InputStream and updates the digest for the data
     *
     * @param digest The MessageDigest to use (e.g. MD5)
     * @param data Data to digest
     * @return the digest
     */
    private fun updateDigest(digest: MessageDigest, data: InputStream): MessageDigest {
        val buffer = ByteArray(STREAM_BUFFER_LENGTH)
        var read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
        while (read > -1) {
            digest.update(buffer, 0, read)
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
        }
        return digest
    }

}


object MessageDigestAlgorithm {
    const val MD2 = "MD2"
    const val MD5 = "MD5"
    const val SHA_1 = "SHA-1"
    const val SHA_224 = "SHA-224"
    const val SHA_256 = "SHA-256"
    const val SHA_384 = "SHA-384"
    const val SHA_512 = "SHA-512"
    const val SHA_512_224 = "SHA-512/224"
    const val SHA_512_256 = "SHA-512/256"
    const val SHA3_224 = "SHA3-224"
    const val SHA3_256 = "SHA3-256"
    const val SHA3_384 = "SHA3-384"
    const val SHA3_512 = "SHA3-512"
}

object StringUtils {

    /**
     * Used to build output as Hex
     */
    private val DIGITS_LOWER =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * Used to build output as Hex
     */
    private val DIGITS_UPPER =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @param toLowerCase `true` converts to lowercase, `false` to uppercase
     * @return A char[] containing hexadecimal characters in the selected case
     */
    fun encodeHex(data: ByteArray, toLowerCase: Boolean): CharArray {
        return encodeHex(data, if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @param toDigits the output alphabet (must contain at least 16 chars)
     * @return A char[] containing the appropriate characters from the alphabet
     *         For best results, this should be either upper- or lower-case hex.
     */
    fun encodeHex(data: ByteArray, toDigits: CharArray): CharArray {
        val l = data.size
        val out = CharArray(l shl 1)
        // two characters form the hex value.
        var i = 0
        var j = 0
        while (i < l) {
            out[j++] = toDigits[0xF0 and data[i].toInt() ushr 4]
            out[j++] = toDigits[0x0F and data[i].toInt()]
            i++
        }
        return out
    }

}
