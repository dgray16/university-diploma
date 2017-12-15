/**
 * <p>
 *     Singlebook package means that to build nested files, one book was used.
 *     It has influence on entropy stats with plain text
 *     (it has almost same entropy number, which is odd for files with different size).
 * </p>
 *
 * <p>
 *     Twobooks package means that to build nested files, two books were used.
 *     It must increase entropy difference between files  with different sizes.
 * </p>
 *
 * <p>
 *     Source package has a single file, that contains bad sequence.
 *     This sequence is used to salt plainttext for weak pseudorandomgenerator test.
 * </p>
 */
package resources.pseudorandomnumbergenerator.weak.text