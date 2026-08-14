package dev.gaphunter.changecasecompanion.actions

import dev.gaphunter.changecasecompanion.model.CaseStyle

class ConvertToCamelCaseAction : ChangeCaseActionBase(CaseStyle.CAMEL)
class ConvertToPascalCaseAction : ChangeCaseActionBase(CaseStyle.PASCAL)
class ConvertToSnakeCaseAction : ChangeCaseActionBase(CaseStyle.SNAKE)
class ConvertToKebabCaseAction : ChangeCaseActionBase(CaseStyle.KEBAB)
class ConvertToConstantCaseAction : ChangeCaseActionBase(CaseStyle.CONSTANT)
