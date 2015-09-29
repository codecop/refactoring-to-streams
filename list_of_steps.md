Steps
=====
This is the list of refactoring steps for the facilitator.

    for /F "usebackq" %a in (`dir /b *.java`) do findstr /i step %a

ExA_Lambdas
-----------
* step1_LambdaExpression
* step2_MethodReference

ExB_Iterating
-------------
* step1_introduceStream
* step2_forEach
* step3_MethodReference

ExC_Collecting
--------------
* step1_introduceStream
* step2_forEach
* step3_collectIntoNewList
* step4_collectorToList

ExD_Mapping
-----------
* step1_introduceStream
* step2_map
* step?_forEach
* step3_collect
* step4_mapMethodReference

ExE_Filtering
-------------
* step1_introduceStream
* step2_filter
* step3_forEach
* step4_collect
* step5_filterMethodReference

ExF_Grouping
------------
* step1_introduceStream
* step2_forEach
* step3_customCollector
* step4_groupBy
* step5_MethodReference

ExG_Finding
-----------
* step1_introduceStream
* step2_filter
* step3_findFirst
* step4_orElseNull

ExH_Summing
-----------
* step1_introduceStream
* step2_forEach
* step3_reduce
* step4_reduceMethodReference
* step5_reduceMethodReferenceInteger
* step6_sum

ExI_SummingRange
----------------
* step1_introduceStream
* step2_forEach
* step3_reduce
* step4_sum
* step5_parallelSum

ExJ_FlatMapping
---------------
* step1_introduceStream
* step2_forEach
* step3_introduceStream2
* step4_forEach2
* step5_boxedCollect
* step6_flatMap
* step7_flatMapMethodReference

ExK_AbortOnException
--------------------
* step1_introduceStream
* step2_forEachWithRethrow
* step?_mapWithRethrow
* step3_mapWithRethrowCollect
* step4_privateException
* step5_MethodReference
* step6_wrappedPrivateException

Alternative
* step4_throwAsUnchecked
* step5_wrapForThrowAsUnchecked
* step6_castToFunctionalInterfaceWithDefaultRethrowAsUnchecked

ExL_IgnoreExceptions
--------------------
* step1_introduceStream
* step2_forEach
* step3a_mapCollectNull
* step3b_mapCollectOptional
* step3c_flatMapCollect
* step4_wrapIgnoreException
* step5_castToFunctionalInterfaceWithDefaultIgnoreException

ExM_Generator
-------------
* fibonacciSupplier

ExX_Grouping2
-------------
* step1_introduceStream
* step2_forEach
* step3_groupBy
* step4_groupByMethodReference
