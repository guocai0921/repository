// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AntPathMatcher.java

package org.springframework.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package org.springframework.util:
//			PathMatcher, Assert, StringUtils

public class AntPathMatcher
	implements PathMatcher
{
	private static class PathSeparatorPatternCache
	{

		private final String endsOnWildCard;
		private final String endsOnDoubleWildCard;

		public String getEndsOnWildCard()
		{
			return endsOnWildCard;
		}

		public String getEndsOnDoubleWildCard()
		{
			return endsOnDoubleWildCard;
		}

		public PathSeparatorPatternCache(String pathSeparator)
		{
			endsOnWildCard = (new StringBuilder()).append(pathSeparator).append("*").toString();
			endsOnDoubleWildCard = (new StringBuilder()).append(pathSeparator).append("**").toString();
		}
	}

	protected static class AntPatternComparator
		implements Comparator
	{
		private static class PatternInfo
		{

			private final String pattern;
			private int uriVars;
			private int singleWildcards;
			private int doubleWildcards;
			private boolean catchAllPattern;
			private boolean prefixPattern;
			private Integer length;

			protected void initCounters()
			{
				for (int pos = 0; pos < pattern.length();)
					if (pattern.charAt(pos) == '{')
					{
						uriVars++;
						pos++;
					} else
					if (pattern.charAt(pos) == '*')
					{
						if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '*')
						{
							doubleWildcards++;
							pos += 2;
						} else
						if (pos > 0 && !pattern.substring(pos - 1).equals(".*"))
						{
							singleWildcards++;
							pos++;
						} else
						{
							pos++;
						}
					} else
					{
						pos++;
					}

			}

			public int getUriVars()
			{
				return uriVars;
			}

			public int getSingleWildcards()
			{
				return singleWildcards;
			}

			public int getDoubleWildcards()
			{
				return doubleWildcards;
			}

			public boolean isLeastSpecific()
			{
				return pattern == null || catchAllPattern;
			}

			public boolean isPrefixPattern()
			{
				return prefixPattern;
			}

			public int getTotalCount()
			{
				return uriVars + singleWildcards + 2 * doubleWildcards;
			}

			public int getLength()
			{
				if (length == null)
					length = Integer.valueOf(AntPathMatcher.VARIABLE_PATTERN.matcher(pattern).replaceAll("#").length());
				return length.intValue();
			}

			public PatternInfo(String pattern)
			{
				this.pattern = pattern;
				if (this.pattern != null)
				{
					initCounters();
					catchAllPattern = this.pattern.equals("/**");
					prefixPattern = !catchAllPattern && this.pattern.endsWith("/**");
				}
				if (uriVars == 0)
					length = Integer.valueOf(this.pattern == null ? 0 : this.pattern.length());
			}
		}


		private final String path;

		public int compare(String pattern1, String pattern2)
		{
			PatternInfo info1 = new PatternInfo(pattern1);
			PatternInfo info2 = new PatternInfo(pattern2);
			if (info1.isLeastSpecific() && info2.isLeastSpecific())
				return 0;
			if (info1.isLeastSpecific())
				return 1;
			if (info2.isLeastSpecific())
				return -1;
			boolean pattern1EqualsPath = pattern1.equals(path);
			boolean pattern2EqualsPath = pattern2.equals(path);
			if (pattern1EqualsPath && pattern2EqualsPath)
				return 0;
			if (pattern1EqualsPath)
				return -1;
			if (pattern2EqualsPath)
				return 1;
			if (info1.isPrefixPattern() && info2.getDoubleWildcards() == 0)
				return 1;
			if (info2.isPrefixPattern() && info1.getDoubleWildcards() == 0)
				return -1;
			if (info1.getTotalCount() != info2.getTotalCount())
				return info1.getTotalCount() - info2.getTotalCount();
			if (info1.getLength() != info2.getLength())
				return info2.getLength() - info1.getLength();
			if (info1.getSingleWildcards() < info2.getSingleWildcards())
				return -1;
			if (info2.getSingleWildcards() < info1.getSingleWildcards())
				return 1;
			if (info1.getUriVars() < info2.getUriVars())
				return -1;
			return info2.getUriVars() >= info1.getUriVars() ? 0 : 1;
		}

		public volatile int compare(Object obj, Object obj1)
		{
			return compare((String)obj, (String)obj1);
		}

		public AntPatternComparator(String path)
		{
			this.path = path;
		}
	}

	protected static class AntPathStringMatcher
	{

		private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
		private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
		private final Pattern pattern;
		private final List variableNames;

		private String quote(String s, int start, int end)
		{
			if (start == end)
				return "";
			else
				return Pattern.quote(s.substring(start, end));
		}

		public boolean matchStrings(String str, Map uriTemplateVariables)
		{
			Matcher matcher = pattern.matcher(str);
			if (matcher.matches())
			{
				if (uriTemplateVariables != null)
				{
					if (variableNames.size() != matcher.groupCount())
						throw new IllegalArgumentException((new StringBuilder()).append("The number of capturing groups in the pattern segment ").append(pattern).append(" does not match the number of URI template variables it defines, which can occur if capturing groups are used in a URI template regex. Use non-capturing groups instead.").toString());
					for (int i = 1; i <= matcher.groupCount(); i++)
					{
						String name = (String)variableNames.get(i - 1);
						String value = matcher.group(i);
						uriTemplateVariables.put(name, value);
					}

				}
				return true;
			} else
			{
				return false;
			}
		}


		public AntPathStringMatcher(String pattern)
		{
			this(pattern, true);
		}

		public AntPathStringMatcher(String pattern, boolean caseSensitive)
		{
			variableNames = new LinkedList();
			StringBuilder patternBuilder = new StringBuilder();
			Matcher matcher = GLOB_PATTERN.matcher(pattern);
			int end;
			for (end = 0; matcher.find(); end = matcher.end())
			{
				patternBuilder.append(quote(pattern, end, matcher.start()));
				String match = matcher.group();
				if ("?".equals(match))
				{
					patternBuilder.append('.');
					continue;
				}
				if ("*".equals(match))
				{
					patternBuilder.append(".*");
					continue;
				}
				if (!match.startsWith("{") || !match.endsWith("}"))
					continue;
				int colonIdx = match.indexOf(':');
				if (colonIdx == -1)
				{
					patternBuilder.append("(.*)");
					variableNames.add(matcher.group(1));
				} else
				{
					String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
					patternBuilder.append('(');
					patternBuilder.append(variablePattern);
					patternBuilder.append(')');
					String variableName = match.substring(1, colonIdx);
					variableNames.add(variableName);
				}
			}

			patternBuilder.append(quote(pattern, end, pattern.length()));
			this.pattern = caseSensitive ? Pattern.compile(patternBuilder.toString()) : Pattern.compile(patternBuilder.toString(), 2);
		}
	}


	public static final String DEFAULT_PATH_SEPARATOR = "/";
	private static final int CACHE_TURNOFF_THRESHOLD = 0x10000;
	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");
	private static final char WILDCARD_CHARS[] = {
		'*', '?', '{'
	};
	private String pathSeparator;
	private PathSeparatorPatternCache pathSeparatorPatternCache;
	private boolean caseSensitive;
	private boolean trimTokens;
	private volatile Boolean cachePatterns;
	private final Map tokenizedPatternCache;
	final Map stringMatcherCache;

	public AntPathMatcher()
	{
		caseSensitive = true;
		trimTokens = false;
		tokenizedPatternCache = new ConcurrentHashMap(256);
		stringMatcherCache = new ConcurrentHashMap(256);
		pathSeparator = "/";
		pathSeparatorPatternCache = new PathSeparatorPatternCache("/");
	}

	public AntPathMatcher(String pathSeparator)
	{
		caseSensitive = true;
		trimTokens = false;
		tokenizedPatternCache = new ConcurrentHashMap(256);
		stringMatcherCache = new ConcurrentHashMap(256);
		Assert.notNull(pathSeparator, "'pathSeparator' is required");
		this.pathSeparator = pathSeparator;
		pathSeparatorPatternCache = new PathSeparatorPatternCache(pathSeparator);
	}

	public void setPathSeparator(String pathSeparator)
	{
		this.pathSeparator = pathSeparator == null ? "/" : pathSeparator;
		pathSeparatorPatternCache = new PathSeparatorPatternCache(this.pathSeparator);
	}

	public void setCaseSensitive(boolean caseSensitive)
	{
		this.caseSensitive = caseSensitive;
	}

	public void setTrimTokens(boolean trimTokens)
	{
		this.trimTokens = trimTokens;
	}

	public void setCachePatterns(boolean cachePatterns)
	{
		this.cachePatterns = Boolean.valueOf(cachePatterns);
	}

	private void deactivatePatternCache()
	{
		cachePatterns = Boolean.valueOf(false);
		tokenizedPatternCache.clear();
		stringMatcherCache.clear();
	}

	public boolean isPattern(String path)
	{
		return path.indexOf('*') != -1 || path.indexOf('?') != -1;
	}

	public boolean match(String pattern, String path)
	{
		return doMatch(pattern, path, true, null);
	}

	public boolean matchStart(String pattern, String path)
	{
		return doMatch(pattern, path, false, null);
	}

	protected boolean doMatch(String pattern, String path, boolean fullMatch, Map uriTemplateVariables)
	{
		if (path.startsWith(pathSeparator) != pattern.startsWith(pathSeparator))
			return false;
		String pattDirs[] = tokenizePattern(pattern);
		if (fullMatch && caseSensitive && !isPotentialMatch(path, pattDirs))
			return false;
		String pathDirs[] = tokenizePath(path);
		int pattIdxStart = 0;
		int pattIdxEnd = pattDirs.length - 1;
		int pathIdxStart = 0;
		int pathIdxEnd = pathDirs.length - 1;
		do
		{
			if (pattIdxStart > pattIdxEnd || pathIdxStart > pathIdxEnd)
				break;
			String pattDir = pattDirs[pattIdxStart];
			if ("**".equals(pattDir))
				break;
			if (!matchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables))
				return false;
			pattIdxStart++;
			pathIdxStart++;
		} while (true);
		if (pathIdxStart > pathIdxEnd)
		{
			if (pattIdxStart > pattIdxEnd)
				return pattern.endsWith(pathSeparator) ? path.endsWith(pathSeparator) : !path.endsWith(pathSeparator);
			if (!fullMatch)
				return true;
			if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(pathSeparator))
				return true;
			for (int i = pattIdxStart; i <= pattIdxEnd; i++)
				if (!pattDirs[i].equals("**"))
					return false;

			return true;
		}
		if (pattIdxStart > pattIdxEnd)
			return false;
		if (!fullMatch && "**".equals(pattDirs[pattIdxStart]))
			return true;
		do
		{
			if (pattIdxStart > pattIdxEnd || pathIdxStart > pathIdxEnd)
				break;
			String pattDir = pattDirs[pattIdxEnd];
			if (pattDir.equals("**"))
				break;
			if (!matchStrings(pattDir, pathDirs[pathIdxEnd], uriTemplateVariables))
				return false;
			pattIdxEnd--;
			pathIdxEnd--;
		} while (true);
		if (pathIdxStart > pathIdxEnd)
		{
			for (int i = pattIdxStart; i <= pattIdxEnd; i++)
				if (!pattDirs[i].equals("**"))
					return false;

			return true;
		}
		do
		{
			if (pattIdxStart == pattIdxEnd || pathIdxStart > pathIdxEnd)
				break;
			int patIdxTmp = -1;
			int i = pattIdxStart + 1;
			do
			{
				if (i > pattIdxEnd)
					break;
				if (pattDirs[i].equals("**"))
				{
					patIdxTmp = i;
					break;
				}
				i++;
			} while (true);
			if (patIdxTmp == pattIdxStart + 1)
			{
				pattIdxStart++;
				continue;
			}
			int patLength = patIdxTmp - pattIdxStart - 1;
			int strLength = (pathIdxEnd - pathIdxStart) + 1;
			int foundIdx = -1;
			int i = 0;
label0:
			do
			{
label1:
				{
					if (i > strLength - patLength)
						break label0;
					for (int j = 0; j < patLength; j++)
					{
						String subPat = pattDirs[pattIdxStart + j + 1];
						String subStr = pathDirs[pathIdxStart + i + j];
						if (!matchStrings(subPat, subStr, uriTemplateVariables))
							break label1;
					}

					foundIdx = pathIdxStart + i;
					break label0;
				}
				i++;
			} while (true);
			if (foundIdx == -1)
				return false;
			pattIdxStart = patIdxTmp;
			pathIdxStart = foundIdx + patLength;
		} while (true);
		for (int i = pattIdxStart; i <= pattIdxEnd; i++)
			if (!pattDirs[i].equals("**"))
				return false;

		return true;
	}

	private boolean isPotentialMatch(String path, String pattDirs[])
	{
		if (!trimTokens)
		{
			char pathChars[] = path.toCharArray();
			int pos = 0;
			String as[] = pattDirs;
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String pattDir = as[j];
				int skipped = skipSeparator(path, pos, pathSeparator);
				pos += skipped;
				skipped = skipSegment(pathChars, pos, pattDir);
				if (skipped < pattDir.length())
					if (skipped > 0)
						return true;
					else
						return pattDir.length() > 0 && isWildcardChar(pattDir.charAt(0));
				pos += skipped;
			}

		}
		return true;
	}

	private int skipSegment(char chars[], int pos, String prefix)
	{
		int skipped = 0;
		char ac[] = prefix.toCharArray();
		int i = ac.length;
		for (int j = 0; j < i; j++)
		{
			char c = ac[j];
			if (isWildcardChar(c))
				return skipped;
			if (pos + skipped >= chars.length)
				return 0;
			if (chars[pos + skipped] == c)
				skipped++;
		}

		return skipped;
	}

	private int skipSeparator(String path, int pos, String separator)
	{
		int skipped;
		for (skipped = 0; path.startsWith(separator, pos + skipped); skipped += separator.length());
		return skipped;
	}

	private boolean isWildcardChar(char c)
	{
		char ac[] = WILDCARD_CHARS;
		int i = ac.length;
		for (int j = 0; j < i; j++)
		{
			char candidate = ac[j];
			if (c == candidate)
				return true;
		}

		return false;
	}

	protected String[] tokenizePattern(String pattern)
	{
		String tokenized[] = null;
		Boolean cachePatterns = this.cachePatterns;
		if (cachePatterns == null || cachePatterns.booleanValue())
			tokenized = (String[])tokenizedPatternCache.get(pattern);
		if (tokenized == null)
		{
			tokenized = tokenizePath(pattern);
			if (cachePatterns == null && tokenizedPatternCache.size() >= 0x10000)
			{
				deactivatePatternCache();
				return tokenized;
			}
			if (cachePatterns == null || cachePatterns.booleanValue())
				tokenizedPatternCache.put(pattern, tokenized);
		}
		return tokenized;
	}

	protected String[] tokenizePath(String path)
	{
		return StringUtils.tokenizeToStringArray(path, pathSeparator, trimTokens, true);
	}

	private boolean matchStrings(String pattern, String str, Map uriTemplateVariables)
	{
		return getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
	}

	protected AntPathStringMatcher getStringMatcher(String pattern)
	{
		AntPathStringMatcher matcher = null;
		Boolean cachePatterns = this.cachePatterns;
		if (cachePatterns == null || cachePatterns.booleanValue())
			matcher = (AntPathStringMatcher)stringMatcherCache.get(pattern);
		if (matcher == null)
		{
			matcher = new AntPathStringMatcher(pattern, caseSensitive);
			if (cachePatterns == null && stringMatcherCache.size() >= 0x10000)
			{
				deactivatePatternCache();
				return matcher;
			}
			if (cachePatterns == null || cachePatterns.booleanValue())
				stringMatcherCache.put(pattern, matcher);
		}
		return matcher;
	}

	public String extractPathWithinPattern(String pattern, String path)
	{
		String patternParts[] = StringUtils.tokenizeToStringArray(pattern, pathSeparator, trimTokens, true);
		String pathParts[] = StringUtils.tokenizeToStringArray(path, pathSeparator, trimTokens, true);
		StringBuilder builder = new StringBuilder();
		boolean pathStarted = false;
label0:
		for (int segment = 0; segment < patternParts.length; segment++)
		{
			String patternPart = patternParts[segment];
			if (patternPart.indexOf('*') <= -1 && patternPart.indexOf('?') <= -1)
				continue;
			do
			{
				if (segment >= pathParts.length)
					continue label0;
				if (pathStarted || segment == 0 && !pattern.startsWith(pathSeparator))
					builder.append(pathSeparator);
				builder.append(pathParts[segment]);
				pathStarted = true;
				segment++;
			} while (true);
		}

		return builder.toString();
	}

	public Map extractUriTemplateVariables(String pattern, String path)
	{
		Map variables = new LinkedHashMap();
		boolean result = doMatch(pattern, path, true, variables);
		if (!result)
			throw new IllegalStateException((new StringBuilder()).append("Pattern \"").append(pattern).append("\" is not a match for \"").append(path).append("\"").toString());
		else
			return variables;
	}

	public String combine(String pattern1, String pattern2)
	{
		if (!StringUtils.hasText(pattern1) && !StringUtils.hasText(pattern2))
			return "";
		if (!StringUtils.hasText(pattern1))
			return pattern2;
		if (!StringUtils.hasText(pattern2))
			return pattern1;
		boolean pattern1ContainsUriVar = pattern1.indexOf('{') != -1;
		if (!pattern1.equals(pattern2) && !pattern1ContainsUriVar && match(pattern1, pattern2))
			return pattern2;
		if (pattern1.endsWith(pathSeparatorPatternCache.getEndsOnWildCard()))
			return concat(pattern1.substring(0, pattern1.length() - 2), pattern2);
		if (pattern1.endsWith(pathSeparatorPatternCache.getEndsOnDoubleWildCard()))
			return concat(pattern1, pattern2);
		int starDotPos1 = pattern1.indexOf("*.");
		if (pattern1ContainsUriVar || starDotPos1 == -1 || pathSeparator.equals("."))
			return concat(pattern1, pattern2);
		String ext1 = pattern1.substring(starDotPos1 + 1);
		int dotPos2 = pattern2.indexOf('.');
		String file2 = dotPos2 != -1 ? pattern2.substring(0, dotPos2) : pattern2;
		String ext2 = dotPos2 != -1 ? pattern2.substring(dotPos2) : "";
		boolean ext1All = ext1.equals(".*") || ext1.equals("");
		boolean ext2All = ext2.equals(".*") || ext2.equals("");
		if (!ext1All && !ext2All)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Cannot combine patterns: ").append(pattern1).append(" vs ").append(pattern2).toString());
		} else
		{
			String ext = ext1All ? ext2 : ext1;
			return (new StringBuilder()).append(file2).append(ext).toString();
		}
	}

	private String concat(String path1, String path2)
	{
		boolean path1EndsWithSeparator = path1.endsWith(pathSeparator);
		boolean path2StartsWithSeparator = path2.startsWith(pathSeparator);
		if (path1EndsWithSeparator && path2StartsWithSeparator)
			return (new StringBuilder()).append(path1).append(path2.substring(1)).toString();
		if (path1EndsWithSeparator || path2StartsWithSeparator)
			return (new StringBuilder()).append(path1).append(path2).toString();
		else
			return (new StringBuilder()).append(path1).append(pathSeparator).append(path2).toString();
	}

	public Comparator getPatternComparator(String path)
	{
		return new AntPatternComparator(path);
	}


}
