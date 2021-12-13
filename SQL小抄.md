# SQL notes

## Execution Order

## WHERE

- WHERE裡不能放aggregation function，像是rank什麼的都只能用having或是額外
- WHERE要擺在GROUP BY 前面
- WHERE 可以用兩個一起
  
    ```sql
    select immediate_percentage 
    from Delivery
    where (customer_id, order_date) in 
        (select customer_id, min(order_date)
        from Delivery
        group by customer_id)
    ```
    
- WHERE裡可以用CASE WHEN
  
    ```sql
    WHERE recent = (CASE WHEN cnt = 1 THEN 1 ELSE 2 END)
    ```

## HAVING

- Having只能放在GROUP BY 後面
- Having裡要寫全名不能用alias
- 可以直接用HAVING篩選，不用先放在COUNT裡

## WINDOW FUNCTIONS

- Window function只能放在select或是order by裡面
- 如果要選擇rank是多少的那個record，只能用subquerry
- **ROWS BETWEEN 2 PRECEDING AND CURRENT ROW**
  
    ```sql
    WITH month_rank AS (
        SELECT 
            Id, Month, 
            SUM(Salary) OVER (PARTITION BY Id ORDER BY Month ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) AS Salary,    
            RANK() OVER (PARTITION BY Id ORDER BY Month DESC) AS rnk,
            COUNT(Month) OVER (PARTITION BY Id) AS total_month
        FROM Employee
    )
    SELECT Id, Month, Salary
    FROM month_rank
    WHERE rnk != 1
    ORDER BY ID, Month DESC
    ```
    
     ***[ROW or RANGE] BETWEEN <start_expr> AND <end_expr>***
    
    <start_expr> can be any one of the following
    
    1. UNBOUNDED PECEDING
    2. CURRENT ROW
    3. <**sql_expr**> PRECEDING or FOLLOWING.
    4. UNBOUNDED FOLLOWING or
    5. CURRENT ROW or
    6. <**sql_expr**> PRECEDING or FOLLOWING.

## TIPS

- 資料注意的點
    - 有沒有重複—> DISTINCT （WINDOW FUNCTION特別容易出現，要注意）
    - 有沒有NULL —>可能要用COALESCE
    - 有沒有整個table是空的—> JOIN的時候要注意
- 如果需要兩種數字計算，先用WITH 寫出兩個table再call，比較準（常常WINDOW function會錯...)
- 只有欄位名稱用 `""`，其他都用 `''`
- 注意重複的資料，可以問有沒有重複
- 記得GROUP BY
    - COUNT()如果沒有東西的話結果是0，其他aggregate都會是NULL
    - 一定要包含出現在aggregate function以外的variables
- 天數記得+1，例如2019-06-30 的90天內—> ADD_MONTHS('2019-06-3',-3)+1;  period_end - period_start +1
- 只有order by 可以用 1,2,3

## OVER

- 會先用PARTITION BY分類一次，所以如果有重複出現的類別，就不能用PARTITION BY 重新編號
- 如果order by 裡有用aggregation function，那就要group by，方便起見還不如把rank放在外面寫
- 用OVER就不能再用GROUP，用DISTINCT 避免重複
  
    ```sql
    --以下兩種相同 （但在count的時候可能會錯？）
    SELECT DISTINCT buyer_id, MIN(order_date) OVER (PARTITION BY buyer_id)
    FROM Orders
    
    SELECT buyer_id, MIN(order_date)
    FROM Orders
    GROUP BY buyer_id
    ```

## Rank

min, max還是要額外弄，不然很有可能變成多個資料都顯示min(year)

放在subquerry

## FETCH NEXT 1 ROW ONLY | WITH TIES

oracle 可用，但在比較後面的版本

```sql
FETCH FIRST 5 PERCENT ROWS ONLY;
FETCH NEXT 10 ROWS WITH TIES;
FETCH FIRST 1 ROW

OFFSET 10 ROWS 
FETCH NEXT 10 ROWS ONLY;
```

## NVL(), COALESCE()

- oracle也可以用coalesce
- NVL也會確認第二個argument可不可以output，例如NVL(1, 1/0)就會出現錯誤，因為0不能被除，可是用COALESCE就沒關係

### UNION vs. UNION ALL

UNION會忽略重複的值

UNION ALL不會忽略重複的值

## CASE WHEN

如果是SUM(CASE WHEN...)記得也要加group by

```sql
WITH summary AS (
    SELECT
        host_team AS team,
        SUM(CASE WHEN host_goals > guest_goals THEN 3 
                 WHEN host_goals = guest_goals THEN 1 
            ELSE 0 END) AS score
    FROM Matches
    GROUP BY host_team
    UNION ALL
    SELECT
        guest_team AS team,
        SUM(CASE WHEN host_goals < guest_goals THEN 3 
                 WHEN host_goals = guest_goals THEN 1 
            ELSE 0 END) AS score
    FROM Matches
    GROUP BY guest_team
)
SELECT
    t.team_id,
    t.team_name,
    COALESCE(SUM(CASE WHEN team = t.team_id THEN score ELSE 0 END),0) AS num_points
FROM summary s
RIGHT JOIN teams t ON t.team_id = s.team
GROUP BY team_id, team_name
ORDER BY num_points DESC, team_id
```

- 不一定要ELSE NULL可以直接 END

```sql
WITH ans_rate AS(
SELECT question_id, RANK() OVER (ORDER BY ans/show_cnt DESC) AS rnk
FROM (
    SELECT 
        question_id, 
        COUNT(CASE WHEN action != 'answer' THEN timestamp END) AS show_cnt, 
        COUNT(answer_id) AS ans
    FROM survey_log
    GROUP BY question_id
)
)
SELECT question_id AS survey_log
FROM ans_rate
WHERE rnk = 1
```

### LAG 前一row

provide additional features for operating on rows related to the currently processed row.

例如想要知道部門中這個人跟下一個比他低薪水的人，就可以用lag來reference row relative to the currently processed

```sql
SELECT 
department, 
last_name, 
salary, 
lag(salary) OVER (PARTITION BY department ORDER BY salary DESC)
FROM staff
```



### LEAD 下一row

就是lag的相反，找前一個的概念

Use the lead function to reference the rows behind currently processed rows.

```sql
SELECT department, last_name, salary, 
lead(salary) OVER (PARTITION BY department ORDER BY salary DESC)
FROM staff

# lead(salary, 2)可以後面幾個
```



## PIVOT TABLE

```sql
SELECT 
    [America] AS America,
    [Asia] AS Asia,
    [Europe] AS Europe
FROM (
    SELECT 
        name, 
        continent,
        ROW_NUMBER() OVER (PARTITION BY continent ORDER BY name) AS rnk -- 要用row_number才可以，不然可能會有重複？
    FROM student
) AS a -- 先在from這邊處理
PIVOT (
    MAX(name) FOR continent IN ([America],[Asia],[Europe])
) AS PIVOTTABLE

SELECT -- 也可以寫 SELECT *
    id, 
    [Jan] AS Jan_Revenue, [Feb] AS Feb_Revenue, [Mar] AS Mar_Revenue, [Apr] AS Apr_Revenue, 
    [May] AS May_Revenue, [Jun] AS Jun_Revenue, [Jul] AS Jul_Revenue, [Aug] AS Aug_Revenue, 
    [Sep] AS Sep_Revenue, [Oct] AS Oct_Revenue, [Nov] AS Nov_Revenue, [Dec] AS Dec_Revenue
FROM Department
PIVOT (
    SUM(revenue) FOR month IN ([Jan],[Feb],[Mar],[Apr],[May],[Jun],[Jul],[Aug], [Sep],[Oct],[Nov],[Dec])
) AS PivotTable
```

## 日期

- 可以直接 `WHERE order_date BETWEEN '2020-02-01' AND '2020-02-29'`
- EXTRACT(YEAR FROM '2020-04-23')

- 找出大寫月份：TO_CHAR(TO_DATE('2020-05-18','YYYY-MM-DD'), 'DAY')
    - DATENAME(month, '2020-04-23') 可能是SQL才能用
    - 查看date time format
    
    [Format Models](https://docs.oracle.com/cd/B19306_01/server.102/b14200/sql_elements004.htm#i34510)
    
- 計算日期：Oracle沒有dateadd，可以用ADD_MONTHS('2020-04-23', -1)
- 指到該區間第一天：
    - TRUNC(start_date, 'MONTH') 該月第一天
    - TRUNC(start_date, 'DAY') 該星期第一天
    - TRUNC也可以用在數字：TRUNC(15.79,1) 小數第一位
- 下一個星期天：NEXT_DAY(start_date, 'SUN')
- 該月最後一天：LAST_DAY(start_date) 該月最後一天

SQL寫法

```sql
SELECT DATEADD(year, 1, '2017/08/25') AS DateAdd;
SELECT DATEDIFF(year, '2017/08/25', '2011/08/25') AS DateDiff;
SELECT DATENAME(year, '2017/08/25') AS DatePartString;
SELECT DATEPART(year, '2017/08/25') AS DatePartInt; -- 用YEAR, MONTH, DAY都可以
```

## RAND

- 隨意排序
  
    ```sql
    SELECT column FROM table
    ORDER BY RAND()
    
    SELECT TOP 1 column FROM table
    ORDER BY NEWID()
    
    SELECT column FROM table
    ORDER BY dbms_random.value
    ```
    
- SQL
  
    隨意產生一組不重複的數，先產生1~100，再用newid() order，再選top多少
    
    ```sql
    WITH RandomNumbers AS (
        -- Anchor member definition
        SELECT  1                         AS row_nm
        UNION ALL
        -- Recursive member definition
        SELECT  rn.row_nm + 1          AS row_nm
        FROM RandomNumbers rn
        WHERE rn.row_nm < 100
    )
    -- Statement that executes the CTE
    SELECT  TOP 50 rn.row_nm                     AS row_nm
            --ROUND(rn.RandomNumber * 1000, 0) AS RoundedRandomNumber
    FROM RandomNumbers rn
    ORDER BY NEWID()
    ```
    
    產生一大組數
    
    ```sql
    -------------------------------------
    -- Generate a set of random numbers
    -- (values between 0 and 1000)
    -------------------------------------
    WITH RandomNumbers AS (
        -- Anchor member definition
        SELECT  1                         AS row_nm, 
                RAND( CHECKSUM( NEWID())) AS RandomNumber -- 拿掉checksum就會失去奇怪的隨機
        UNION ALL
        -- Recursive member definition
        SELECT  rn.row_nm + 1          AS row_nm, 
                RAND( CHECKSUM( NEWID())) AS RandomNumber
        FROM RandomNumbers rn
        WHERE rn.row_nm < 10
    )
    -- Statement that executes the CTE
    SELECT  rn.row_nm                     AS row_nm,
            ROUND(rn.RandomNumber * 1000, 0) AS RoundedRandomNumber
    FROM RandomNumbers rn
    OPTION (MAXRECURSION 1000); -- 如果要產生超過100個數就需要這個，如果要表示沒有limit那就用0
    ```
    
    產生一個區間的數，就是*（尾-頭)+頭
    
    ```sql
    SELECT RAND(100) --可以用seed的概念
    -- 產生 >=5, <10的有小數點數字
    SELECT RAND()*(10-5)+5;
    -- 產生 >= 5 and <=10 的整數(floor小於該數的最大整數); 反之ceiling是大於該數的最小整數
    -- 因為要有
    SELECT FLOOR(RAND()*(10-5+1)+5);
    ```
    
- ORACLE專用
  
    可以透過一個database來產生random number or string
    
    [https://www.databasejournal.com/features/oracle/article.php/3341051/Generating-random-numbers-and-strings-in-Oracle.htm](https://www.databasejournal.com/features/oracle/article.php/3341051/Generating-random-numbers-and-strings-in-Oracle.htm)
    
    ```sql
    -- Generating a random number (positive or negative)
    select dbms_random.random from dual;
    -- 1393936551
    
    -- Generating a random number between 0 and 1.
    select dbms_random.value from dual;
    -- 1
    
    -- Generating a random number from a range, between 1 to 1000.
    select dbms_random.value(1,1000) num from dual;
    -- 611
    
    -- Generating a 12 digit random number.
    select dbms_random.value(100000000000, 999999999999) num from dual;
    -- 175055628780
    
    -- Generating an upper case string of 20 characters
    select dbms_random.string('U', 20) str from dual;
    -- VUOQOSTLHCKIPIADIZTD
    
    -- Generating a lower case string of 20 characters
    select dbms_random.string('L', 20) str from dual;
    -- xpoovuspmehvcptdtzcz
    
    -- Generating an alphanumeric string of 20 characters. There is a bug in Oracle 8i that results in special (non-alphanumeric) characters such as ']' in the string. This is resolved in Oracle 9i.
    select dbms_random.string('A', 20) str from dual;
    -- sTjERojjL^OlTaIc]PLB
    
    -- Generating an upper case alphanumeric string of 20 characters
    select dbms_random.string('X', 20) str from dual;
    -- SQ3E3B3NRBIP:GOGAKSC
    
    -- Generating a string of printable 20 characters. This will output a string of all characters that could possibly be printed.
    select dbms_random.string('P', 20) str from dual;
    -- *Yw>IKzsj\uI8K[IQPag
    ```

## 參數 parameter

- Oracle 用:表示
  
    ```sql
    DECLARE
      l_product_name VARCHAR2( 100 ) := 'Laptop';
    	l_product_name VARCHAR2( 100 ) DEFAULT 'Laptop'; --也可以用default，要不要assign value都可以
    BEGIN
      NULL;
    END;
    ```
    
- SQL Server
  
    ```sql
    DECLARE @Group nvarchar(50), @Sales money;  
    SET @Group = N'North America';
    ```

## Stored procedures

- SQL Server
  
    ```sql
    CREATE PROCEDURE HumanResources.uspGetEmployeesTest2   
        @LastName nvarchar(50),   
        @FirstName nvarchar(50)   
    AS   
        SET NOCOUNT ON;  
        SELECT FirstName, LastName, Department  
        FROM HumanResources.vEmployeeDepartmentHistory  
        WHERE FirstName = @FirstName AND LastName = @LastName  
        AND EndDate IS NULL;  
    GO
    
    EXECUTE HumanResources.uspGetEmployeesTest2
    ```

## Function

- Oracle
  
    記得要declare 
    
    ```sql
    CREATE FUNCTION getNthHighestSalary(N IN NUMBER) RETURN NUMBER IS
    result NUMBER;
    BEGIN
        SELECT 
            Salary INTO result
        FROM (
            SELECT 
                Salary, 
                DENSE_RANK() OVER (ORDER BY Salary DESC) AS rnk
            FROM Employee
        )
        WHERE rnk = N; --記得要;
        RETURN result;
    END;
    ```
    
- SQL Server
  
    ```sql
    CREATE FUNCTION Sales.ufn_SalesByStore (@storeid int)
    RETURNS TABLE
    AS
    RETURN
    (
        SELECT P.ProductID, P.Name, SUM(SD.LineTotal) AS 'Total'
        FROM Production.Product AS P
        JOIN Sales.SalesOrderDetail AS SD ON SD.ProductID = P.ProductID
        JOIN Sales.SalesOrderHeader AS SH ON SH.SalesOrderID = SD.SalesOrderID
        JOIN Sales.Customer AS C ON SH.CustomerID = C.CustomerID
        WHERE C.StoreID = @storeid
        GROUP BY P.ProductID, P.Name
    );
    GO
    
    SELECT * FROM Sales.ufn_SalesByStore (602); -- 直接用在select 裡面
    ```

盡量讓Analytics跟Transaction在不同地方，這樣分析的時候才不會影響

[CheatSheet: SQL & MySql](https://cheatsheet.dennyzhang.com/cheatsheet-mysql-A4)

## JOIN

natural join

cross join

## REGEXP

- REGEX_LIKE在WHERE裡
  
    ```sql
    where REGEXP_LIKE(order_date,'2019-08-[01-20]')
    ```
    
- REGEXP_REPLACE
- REGEXP_INSTR
- REGEXP_SUBSTR



## STRING_SPLIT & SUBSTRING

SQL server用

針對面試那題怎麼parse column寫的解答

item        product_group     description

iphone	3	length:10;width:5;l_unit:mm;w_unit:lbs
iphone12	1	length:80;width:25;l_unit:kg;w_unit:lbs
iphone9	4	length:5;width:65;l_unit:km;w_unit:lbs

寫法是先透過STRING_SPLIT把不同內容分開，再用SUBSTRING擷取欄位名稱跟值，最後用pivot table 變成理想的形式

```sql
SELECT 
    item,
    product_group,
    [length],
    [width],
    [l_unit],
    [w_unit]
FROM (
    SELECT 
        item, product_group,
        SUBSTRING(first_time,1,CHARINDEX(':',first_time)-1) AS col,
        SUBSTRING(first_time,CHARINDEX(':',first_time)+1,len(first_time)) AS value
    FROM (
        SELECT item, product_group, value AS first_time
        FROM cleaning 
            CROSS APPLY STRING_SPLIT(description, ';')
    ) a
) b
PIVOT 
(
    MAX(value) FOR col IN ([length],[width],[l_unit],[w_unit]) -- 這裡一定要有一個aggregate function
) AS pivottable
```