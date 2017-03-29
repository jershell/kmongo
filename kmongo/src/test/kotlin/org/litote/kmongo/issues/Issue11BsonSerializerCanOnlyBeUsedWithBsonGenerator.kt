/*
 * Copyright (C) 2016 Litote
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.litote.kmongo.issues

import org.bson.Document
import org.junit.Test
import org.litote.kmongo.KMongoBaseTest
import org.litote.kmongo.MongoId
import org.litote.kmongo.findOneById
import org.litote.kmongo.issues.Issue11BsonSerializerCanOnlyBeUsedWithBsonGenerator.ClassWithIntId
import org.litote.kmongo.withDocumentClass
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 *
 */
class Issue11BsonSerializerCanOnlyBeUsedWithBsonGenerator : KMongoBaseTest<ClassWithIntId>() {

    data class ClassWithIntId(@MongoId val id: Int, val title: String?)

    override fun getDefaultCollectionClass(): KClass<ClassWithIntId> {
        return ClassWithIntId::class
    }

    @Test
    fun testSerializeAndDeserialize() {
        val document = Document.parse("""{ "_id" : 42, "url" : "somewhere.com", "updated" : ISODate("2016-07-29T14:32:55.123Z"), "title" : "United States Post Office" }""")
        col.withDocumentClass<Document>().insertOne(document)
        val document2 = col.findOneById(42)
        assertNotNull(document2)
        assertEquals("United States Post Office", document2!!.title)
        assertEquals(42, document2.id)
    }
}