import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main(args: Array<String>) {
}
fun findMaxForm(strs: Array<String>, m: Int, n: Int): Int {
    val len = strs.size
    var d3 = Array<Array<Array<Int>>>(len + 1) {
        Array<Array<Int>>(m + 1) {
            Array<Int>(n + 1) { 0 }
        }
    }
    for (i in 1..len) {
        var count = Array<Int>(2) { 0 }
        for (x in strs[i - 1]) {
            if (x == '0') count[0]++;
            else count[1]++
        }
        for (j in 0..m) {
            for (k in 0..n) {
                d3[i][j][k] = if (j >= count[0] && k >= count[1]) {
                    Math.max(d3[i - 1][j][k], d3[i - 1][j - count[0]][k - count[1]] + 1)
                } else {
                    d3[i - 1][j][k]
                }
            }
            //  println()
        }
        // println()
    }
    return d3[len][m][n];
}

// https://leetcode.com/problems/friends-of-appropriate-ages/
fun numFriendRequests(ages: IntArray): Int {
    var ageMap = Array<Int>(121) { 0 }
    var sumMap = Array<Int>(121) { 0 }
    var res = 0
    for (i in ages) ageMap[i]++

    for (i in 1..120) sumMap[i] = ageMap[i] + sumMap[i - 1]
    for (i in 15..120) {
        if (ageMap[i] == 0) continue
        val count = sumMap[i] - sumMap[i / 2 + 7]
        res = res + count * ageMap[i] - ageMap[i]
    }
    return res
}
//https://leetcode.com/problems/course-schedule/

fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    if (numCourses == 1 || prerequisites.size == 0) true
    var mat = Array<ArrayList<Int>>(numCourses) { ArrayList<Int>() }


    for (i in 0..prerequisites.size - 1) {
        val course = prerequisites[i][0]
        val pre_course = prerequisites[i][1]
        mat.get(pre_course).add(course)
    }

    var visited = Array<Int>(numCourses) { 0 }

    for (i in 0..mat.size - 1) {
        val res = if (dfs(visited, mat, i)) true else false
        if (res == false) return false
    }
    return true;
}

fun dfs(visited: Array<Int>, mat: Array<ArrayList<Int>>, start: Int): Boolean {

    visited[start] = 1
    for (i in 0..mat.get(start).size - 1) {
        val index = mat.get(start).get(i)
        if (visited[index] == 1) return false;
        val rt = dfs(visited, mat, index)
        if (rt == false) return false;
    }
    visited[start] = 0
    return true;
}

//https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/
class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

fun removeZeroSumSublists(head: ListNode?): ListNode? {
    if (head == null) return null

    var sum_map = HashMap<Int, ListNode>()

    var sum = 0
    val addhead = ListNode(0)
    var crawl: ListNode? = addhead
    addhead.next = head
    while (crawl != null) {
        sum = sum + crawl.`val`
        sum_map.put(sum, crawl)
        crawl = crawl.next
    }
    sum = 0
    crawl = addhead
    while (crawl != null) {
        sum = sum + crawl.`val`
        crawl.next = sum_map.get(sum)?.next
        crawl = crawl.next
    }
    return addhead.next
}

//https://leetcode.com/problems/partition-list/
fun partition(head: ListNode?, x: Int): ListNode? {
    if (head == null) return null
    var que = ArrayDeque<ListNode>()
    var crawl = head;
    var addhead = ListNode(0)
    addhead.next = head
    var last = addhead
    while (crawl != null) {
        if (crawl.`val` >= x) {
            last.next = crawl.next
            crawl.next = null
            que.add(crawl)
            crawl = last.next
        } else {
            last = crawl
            crawl = crawl.next
        }
    }
    while (!que.isEmpty()) {
        val node = que.poll()
        last.next = node
        last = node
    }
    return addhead.next
}

//https://leetcode.com/problems/find-k-closest-elements/
fun findClosestElements(arr: IntArray, k: Int, x: Int): List<Int> {
    var res = ArrayList<Int>()
    if (x <= arr[0]) {
        for (i in 0..k - 1) res.add(arr[i])
        return res
    }
    if (x >= arr[arr.size - 1]) {
        for (i in arr.size - k..arr.size - 1) res.add(arr[i])
        return res
    }
    var diff = Integer.MIN_VALUE
    var index = -1
    for (i in 0..arr.size - 2) {
        if (Math.abs(x - arr[i]) < diff) {
            diff = Math.abs(x - arr[i])
            index = i
        }
    }

    var start = index
    var last = index
    while (start - last != k - 1) {
        var left = if (start - 1 >= 0) {
            arr[start - 1]
        } else {
            Integer.MAX_VALUE
        }
        var right = if (last + 1 < arr.size) {
            arr[last + 1]
        } else {
            Integer.MAX_VALUE
        }
        if (Math.abs(x - left) <= Math.abs(x - right)) start = start - 1 else last = last + 1
    }

    for (i in start..last) {
        res.add(arr[start])
        start++
    }
    return res
}

//https://leetcode.com/problems/prison-cells-after-n-days/
fun prisonAfterNDays(cells: IntArray, N: Int): IntArray {
    var X = N
    var cell = cells
    var map = HashMap<String, Int>()
    while (X > 0) {
        var arr = IntArray(8) { 0 }
        map[Arrays.toString(cell)] = X
        X--
        for (i in 1..6) {
            arr[i] = if (cell[i - 1] == cell[i + 1]) 1 else 0
        }
        cell = arr
        if (map.containsKey(Arrays.toString(cell))) {
            val index = map.get(Arrays.toString(cell))
            println(index)
            X = X % (index?.minus(X))!!
        }
    }
    return cell
}

//https://leetcode.com/problems/course-schedule-ii/
// this is graphy topological sort problem
fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
    var res: IntArray = IntArray(numCourses)
    if (numCourses == 1) return res
    if (prerequisites.size == 0) {
        for (i in 0..numCourses - 1) res[i] = i
        return res
    }

    var mat = Array<ArrayList<Int>>(numCourses) { ArrayList<Int>() }


    for (i in 0..prerequisites.size - 1) {
        val course = prerequisites[i][0]
        val pre_course = prerequisites[i][1]
        mat.get(pre_course).add(course)
    }
    var stack = Stack<Int>()
    var visited = Array<Int>(numCourses) { 0 }
    for (i in 0..mat.size - 1) {
        val res = if (dfs(visited, mat, i)) true else false
        if (res == false) return IntArray(0)
    }
    visited.fill(0,0,visited.size)
    for(i in 0..mat.size -1){
        if(visited[i] == 0){
            graphTopologicalSort(visited,mat,i,stack)
        }
    }
    for( i in 0..res.size -1 ) res[i] = if(!stack.empty()) stack.pop() else 0
    return res;
}

fun graphTopologicalSort(visited: Array<Int>, mat: Array<ArrayList<Int>>, vertex: Int, stack: Stack<Int>){
    visited[vertex]  = 1
    var ret = true
    for(i in mat.get(vertex)){
        if (visited[i] == 0)
            graphTopologicalSort(visited, mat, i, stack)

    }
    stack.push(vertex)
}

//https://leetcode.com/discuss/interview-question/351783/