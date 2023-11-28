package pkg;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CodeReview {
	String reviewId;
	String ownerId;
	String createdOn;
	String projectPath;
	int updatedScore;
	Map<Integer, CodeReview> dict = new LinkedHashMap<Integer, CodeReview>();;
   //Constructor 
	CodeReview() {
		reviewId = "";
		updatedScore = 0;
		ownerId = "";
		createdOn = "";
		projectPath = "";

	}
    //Calculating longest common suffix by passing Map and string, where we will iterate all entries of map and compare with the string
	public void LongestCommonSuffix(Map<Integer, CodeReview> dict, String path) {
		for (CodeReview freqObject : dict.values()) {
			String projectPathTokens[] = freqObject.projectPath.split("/");
			String pathTokens[] = path.split("/");
			int j = projectPathTokens.length - 1;

			for (int i = 0; i <= projectPathTokens.length / 2; i++) {
				String temp = projectPathTokens[i];
				projectPathTokens[i] = projectPathTokens[j];
				projectPathTokens[j] = temp;
				j--;
			}

			j = pathTokens.length - 1;
			for (int i = 0; i <= pathTokens.length / 2; i++) {
				String temp = pathTokens[i];
				pathTokens[i] = pathTokens[j];
				pathTokens[j] = temp;
				j--;
			}

			int count = 0;
			for (int itr = 0; itr < Math.min(projectPathTokens.length, pathTokens.length); itr++) {
				if (projectPathTokens[itr].equals(pathTokens[itr])) {
					count++;
				} else {
					break;
				}
			}
			freqObject.updatedScore += count;
		}
	}
	 //Calculating longest common prefix by passing Map and string, where we will iterate all entries of map and compare with the string
	public void LongestCommonPrefix(Map<Integer, CodeReview> dict, String path) {
		for (CodeReview freqObject : dict.values()) {
			String projectPathTokens[] = freqObject.projectPath.split("/");
			String pathTokens[] = path.split("/");

			int count = 0;
			for (int itr = 0; itr < Math.min(projectPathTokens.length, pathTokens.length); itr++) {
				if (projectPathTokens[itr].equals(pathTokens[itr])) {
					count++;
				} else {
					break;
				}
			}
			freqObject.updatedScore += count;
			}
	}
    //getter for returning the value of updated score as it will be required when we will be sorting the map based on updatedScore in descending order
	public int getUpdatedScore() {
		return updatedScore;
	}
    
	public int LCS(String str1[], String str2[], int len1, int len2) {
		if (str1.length == len1 || str2.length == len2)
			return 0;
		else if (str1[len1].equals(str2[len2]))
			return 1 + LCS(str1, str2, len1 + 1, len2 + 1);
		else
			return Math.max(LCS(str1, str2, len1 + 1, len2), LCS(str1, str2, len1, len2 + 1));
	}

	public int LCSubstr(String str1[], String str2[]) {
		int max_count = 0;
		int count = 0;
		for (int itr = 0; itr < Math.min(str1.length, str2.length); itr++) {
			if (str1[itr].equals(str2[itr])) {
				count++;
			} else {
				max_count = Math.max(max_count, count);
				count = 0;
			}
		}
		return max_count;
	}
	//Calculating longest common substring by passing Map and string, where we will iterate all entries of map and compare with the string
	public void LongestCommonSubstring(Map<Integer, CodeReview> dict, String path) {
		for (CodeReview freqObject : dict.values()) {
			String projectPathTokens[] = freqObject.projectPath.split("/");
			String pathTokens[] = path.split("/");
			int count = LCSubstr(projectPathTokens, pathTokens);
			freqObject.updatedScore += count;
		}
	}
	 //Calculating longest common subsequence by passing Map and string, where we will iterate all entries of map and compare with the string
	public void LongestCommonSubsequence(Map<Integer, CodeReview> dict, String path) {
		for (CodeReview freqObject : dict.values()) {
			String projectPathTokens[] = freqObject.projectPath.split("/");
			String pathTokens[] = path.split("/");
			int count = LCS(projectPathTokens, pathTokens, 0, 0);
			freqObject.updatedScore += count;
		}
	}
	public void cleanDict() {
		for (CodeReview freqObject : dict.values()) {
			freqObject.updatedScore = 0;
		}
	}
    //Iterating map and storing value in list, sorting based on updated score and then printing top 10 values which have high value of updated score
	public void printDict(String line) throws IOException {
		// Convert the values of the dictionary to a list
		List<CodeReview> reviewList = new ArrayList<>(dict.values());
		for (CodeReview freqObject : dict.values()) {
			freqObject.updatedScore %= Math.max(line.length(), freqObject.projectPath.length());
		}
		// Sort the list by updatedScore in descending order
		reviewList.sort(Comparator.comparingInt(CodeReview::getUpdatedScore).reversed());
		String outFile = "F:\\University\\Semester1\\SE\\Project\\count.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile,true));
		String output = "";
		// Print the sorted list
		output += "For the following path " + line + " these can be the reviewers:\n";
		int i=0;
		for (CodeReview freqObject : reviewList) {
			if(i < 10) {
			output += "Review Id= " + freqObject.reviewId + " Owner Id= " +
					freqObject.ownerId + " Create date= " + freqObject.createdOn + " Project Path= " +
					freqObject.projectPath + " Updated score= " + freqObject.updatedScore + "\n";
			i++;
			}
			else
				break;
		}
		output += "\n";
		bw.write(output);
		bw.close();

	}
    //flow starts from here reading csv file one by one storing in map and then calling subsequence, substring, prefix, suffix method for calculating updated score for each entry and printing top 10 records with high value of updated score
	public static void main(String[] args) throws IOException {
		String line = "";
		CodeReview review = new CodeReview();
		int key = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(
				new File("C:\\Users\\Dell\\eclipse-workspace\\CodeReviewSystem\\src\\pkg\\input.csv")))) {
			while ((line = br.readLine()) != null) {
				String[] details = line.split(",");
				CodeReview existingReview = review.dict.get(key);
				if (existingReview == null) {
					existingReview = new CodeReview();
					review.dict.put(key, existingReview);
				}
				for (int i = 0; i < details.length; i++) {
					if (i == 0) {
						existingReview.reviewId = details[i];
					} else if (i == 1) {
						existingReview.ownerId = details[i];
					} else if (i == 2) {
						existingReview.createdOn = details[i];
					} else if (i == 3) {
						existingReview.projectPath = details[i];
					}
				}

				key++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	 	line="";
	 
		int j=0;
		try (BufferedReader br = new BufferedReader(new FileReader(
				new File("F:\\University\\Semester1\\SE\\Project\\input.txt")))) {
			while ((line = br.readLine()) != null) {
				System.out.println(j);
				j++;
				review.cleanDict();
				review.LongestCommonPrefix(review.dict, line);
				review.LongestCommonSuffix(review.dict, line);
				review.LongestCommonSubsequence(review.dict, line);
				review.LongestCommonSubstring(review.dict, line);
				System.out.println(line + " inside main");
				review.printDict(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}