/*
 * 测试设计原则：采用等价类划分与边界值分析。覆盖无零、单零、多零、
 * 混合符号、重复元素及数组长度、整数取值边界。再用固定种子生成小数组，
 * 以逐项跳过自身的直接乘法为参照，独立检验前缀与后缀算法。
 */
/*
 * Review 评价：
 * 测试类命名符合 L学号_题号_Test 规则，类开头给出了等价类划分与边界值分析的
 * 总体原则，每个方法前也标注了测试目的与输入输出，结构清晰、可读性好。
 * 用例覆盖完整：除题目示例外，对单零、双零、负数与重复元素、单元素、空数组
 * 等典型等价类分别验证，并覆盖长度 1/2 以及 int 的 MIN、MAX 取值边界。
 * 最大的亮点是 agreesWithDirectMultiplication：用固定种子 18 生成长度 1~8、
 * 每个长度 30 组、共 240 个可复现的随机用例，并以独立的直接乘法作为参照
 * （test oracle），属于带性质检验的随机测试，显著提升了结果可信度。
 * 两点小建议：
 * 1. 当前使用 JUnit 4（org.junit.Test），若项目统一为 JUnit 5 Jupiter 可考虑
 *    迁移，不影响当前运行；
 * 2. 随机用例元素限定在 [-3,3]，未覆盖大整数相乘的溢出情形，若题目要求考虑
 *    int 溢出，可再补一组接近 Integer.MAX_VALUE 的用例。
 */
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class L2024111842_18_Test {
    private final Solution solution = new Solution();

    // 验证题目示例 1 的首项、中间项和尾项：[1, 2, 3, 4] → [24, 12, 8, 6]。
    @Test
    public void positiveExample() {
        assertArrayEquals(new int[]{24, 12, 8, 6},
                solution.productExceptSelf(new int[]{1, 2, 3, 4}));
    }

    // 验证一个零时只有零所在位置可非零：[-1, 1, 0, -3, 3] → [0, 0, 9, 0, 0]。
    @Test
    public void singleZeroExample() {
        assertArrayEquals(new int[]{0, 0, 9, 0, 0},
                solution.productExceptSelf(new int[]{-1, 1, 0, -3, 3}));
    }

    // 验证两个零时所有位置都为零：[0, 5, 0, -2] → [0, 0, 0, 0]。
    @Test
    public void twoZeros() {
        assertArrayEquals(new int[]{0, 0, 0, 0},
                solution.productExceptSelf(new int[]{0, 5, 0, -2}));
    }

    // 验证负号和重复元素：[-2, 3, -2] → [-6, 4, -6]。
    @Test
    public void mixedSignsAndDuplicates() {
        assertArrayEquals(new int[]{-6, 4, -6},
                solution.productExceptSelf(new int[]{-2, 3, -2}));
    }

    // 验证长度为 2 时保留另一个元素，并覆盖 int 两个端点：[MIN, 1] 与 [1, MAX]。
    @Test
    public void twoElementsAtIntegerLimits() {
        assertArrayEquals(new int[]{1, Integer.MIN_VALUE},
                solution.productExceptSelf(new int[]{Integer.MIN_VALUE, 1}));
        assertArrayEquals(new int[]{Integer.MAX_VALUE, 1},
                solution.productExceptSelf(new int[]{1, Integer.MAX_VALUE}));
    }

    // 验证单元素的空乘积为 1：[0] → [1]。
    @Test
    public void singleton() {
        assertArrayEquals(new int[]{1}, solution.productExceptSelf(new int[]{0}));
    }

    // 验证空数组扩展边界：[] → []。
    @Test
    public void emptyArray() {
        assertArrayEquals(new int[0], solution.productExceptSelf(new int[0]));
    }

    // 对照直接乘法：种子 18，长度 1～8，每个长度 30 组，元素在 [-3, 3]。
    @Test
    public void agreesWithDirectMultiplication() {
        Random random = new Random(18);
        for (int length = 1; length <= 8; length++) {
            for (int sample = 0; sample < 30; sample++) {
                int[] nums = random.ints(length, -3, 4).toArray();
                int[] expected = new int[length];
                for (int i = 0; i < length; i++) {
                    int product = 1;
                    for (int j = 0; j < length; j++) {
                        if (i != j) {
                            product *= nums[j];
                        }
                    }
                    expected[i] = product;
                }
                assertArrayEquals("length " + length + ", sample " + sample,
                        expected, solution.productExceptSelf(nums));
            }
        }
    }
}
