1. 首先，创建一个RuleInfoDTO的实例。
2. 初始化。创建一个xmlRule实例，构造函数参数是xml文件目录名，主花色和主级牌。生成之后，调用xmlRule.initlizeRulInfo(), 参数是一个RuleInFoDTO, RuleInfoDTO 初始化完成
3. 创建CardComparator实例和CheckTypeLegal实例，构造函数式一个RulInfoDTO
4. 检查类型的时候，调用checktypelegal.check(string[] basecard, string[] handcard, string[] playcard)。（斗地主中不出牌默认合法）
5. 比较大小的时候，调用cardcomparator.isbigthan(string[] basecard, string[] card1, string[] card2). 返回结果card1是否大于card2.

仅做了常见情况的测试。如果大家在使用的过程中遇到bug。请及时联系我。