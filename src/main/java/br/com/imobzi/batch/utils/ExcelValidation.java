package br.com.imobzi.batch.utils;

import br.com.imobzi.batch.handler.BadRequestException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.ObjectUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExcelValidation {
	
	public static List<Cell> getCells(Row row){
		List<Cell> cells = new ArrayList<Cell>();
		for (int index = 0; index < 29; index++) {
			cells.add(row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));			
		}
		return cells;
	}
	
	public static List<Cell> validCellsType(List<Cell> cells) {
		IntStream.range(0, cells.size())
		  .forEach(index -> {
			if(index == 0) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 1) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 2) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 3) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 4) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 5) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 6) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 7) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 8) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 9) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 10) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 11) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 12) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 13) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 14) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 15) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 16) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 17) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 18) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 19) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 20) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 21) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 22) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 23) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 24) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 25) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 26) {
				cells.get(index).setCellType(CellType.STRING);
			}
			else if(index == 27) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
			else if(index == 28) {
				cells.get(index).setCellType(CellType.NUMERIC);
			}
		  });
		return cells;
	}

    public static Boolean validBuilding(String string) {
        if (ObjectUtils.isEmpty(string)) {
            return false;
        }
        return true;
    }

    public static String validAddress(String valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Endereço nulo ou inválido");
    }

    public static String validCity(String valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Cidade nula ou inválido");
    }

    public static String validCountry(String valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Pais nulo ou inválido");
    }

    public static String validFinality(String valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            if (valid.toLowerCase().equals("residential") || valid.toLowerCase().equals("commercial")
                    || valid.toLowerCase().equals("rural")) {
                return valid;
            }
        }
        throw new BadRequestException("tipo não pode ser nula ou inválido");
    }

    public static String validNeighborhood(String valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Vizinhança nula ou inválido");
    }

    public static String validZipcode(Integer valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid.toString();
        }
        throw new BadRequestException("CEP nula ou inválido");
    }

    public static Double validRentalValue(Double valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Valor da locação não pode ser nul ou está inválido");
    }

    public static Double validSaleValue(Double valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Valor da locação não pode ser nul ou está inválido");
    }

    public static String validState(String valid) {
        if (!ObjectUtils.isEmpty(valid)) {
            return valid;
        }
        throw new BadRequestException("Estado não pode ser nulo ou está inválido");
    }
    
    public static String validAlternativeCode(Cell cell) {
    	if(cell.getCellType() ==  CellType.NUMERIC) {
    		cell.setCellType(CellType.STRING);
    	}
    	return cell.getStringCellValue();
    }

    public static String formatTextWithAllColumns(String text,
                                          String address,
                                          String addressComplement,
                                          String neighborhood,
                                          String city,
                                          String state,
                                          Integer zipcode,
                                          String country,
                                          String finality,
                                          String propertyType,
                                          String buildingName,
                                          String alternativeCode,
                                          Integer bedroom,
                                          Integer suite,
                                          Integer bathroom,
                                          Integer garage,
                                          Integer usefulArea,
                                          Integer lotArea,
                                          Integer area,
                                          String descriptionExcel,
                                          Double saleValue,
                                          Double rentalValue,
                                          Double iptu,
                                          Integer built,
                                          String owners,
                                          String captador,
                                          String photos,
                                          String multimidias,
                                          Double avaliation_value,
                                          Double discount) {

        if (!ObjectUtils.isEmpty(text)) {
            String textFinal = MessageFormat
                    .format(text, address,addressComplement,neighborhood,city,
                            state,zipcode,country,finality,propertyType,buildingName,
                            alternativeCode,bedroom,suite,bathroom,garage,usefulArea,
                            lotArea,area,descriptionExcel,saleValue,rentalValue,iptu,built,owners,captador,
                            photos,multimidias,avaliation_value,discount);
            return textFinal;
        }
        return descriptionExcel;
    }
}
