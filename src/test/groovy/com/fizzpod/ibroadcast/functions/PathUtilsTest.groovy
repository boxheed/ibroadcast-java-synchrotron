package com.fizzpod.ibroadcast.functions

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils

import spock.lang.Specification

public class PathUtilsTest extends Specification {

    def "basic test with unix paths"() {
        expect:
        PathUtils.cut(a, b) == c

        where:
        a | b || c
        "/abc/def/hij/" | "/abc/def/hij/klm/nop/" || "klm/nop"
        "/abc/def/hij/" | "/abc/def/hij/klm/nop" || "klm/nop"
        "/abc/def/hij" | "/abc/def/hij/klm/nop/" || "klm/nop"
        "/abc/def/hij" | "/abc/def/hij/klm/nop" || "klm/nop"
        "/abc/def/hij" | "abc/def/hij/klm/nop" || "klm/nop"
        "abc/def/hij" | "/abc/def/hij/klm/nop" || "klm/nop"
        "\\abc\\def\\hij\\" | "\\abc\\def\\hij\\klm\\nop\\" || "klm/nop"
        "\\abc\\def\\hij\\" | "\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "\\abc\\def\\hij" | "\\abc\\def\\hij\\klm\\nop\\" || "klm/nop"
        "\\abc\\def\\hij" | "\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "\\abc\\def\\hij" | "abc\\def\\hij\\klm\\nop" || "klm/nop"
        "abc\\def\\hij" | "\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "C:\\abc\\def\\hij" | "C:\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "C:\\abc\\def\\hij" | "D:\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "C:/abc/def/hij" | "D:\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "C:/abc/def/hij" | "D:/abc/def/hij/klm/nop" || "klm/nop"
        "/abc/def/hij" | "D:\\abc\\def\\hij\\klm\\nop" || "klm/nop"
        "C:\\abc\\def\\hij" | "/abc/def/hij/klm/nop" || "klm/nop"
        "C:\\abc\\def\\hij" | "/abc/def/hij/klm/nop/qrs.txt" || "klm/nop/qrs.txt"
        "/abc/def/hij/" | "/klm/nop/" || "klm/nop"
        "./abc/def/hij/" | "/abc/def/hij/klm/nop/" || "klm/nop"
        "../abc/def/hij/" | "/abc/def/hij/klm/nop/" || "klm/nop"
        "../abc/def/hij/" | "../abc/def/hij/klm/nop/" || "klm/nop"
        "\\abc\\def\\hij\\" | "hij\\klm\\nop\\" || "klm/nop"
        
        
        


    }

}