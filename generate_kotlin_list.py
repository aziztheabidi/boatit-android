#!/usr/bin/env python3
"""
Python script to generate a comprehensive list of all Kotlin files in the project.
"""

import os
import sys
from pathlib import Path

def find_kotlin_files(root_dir):
    """Find all .kt files in the project with line counts."""
    kotlin_files = []
    root_path = Path(root_dir)
    
    for file_path in root_path.rglob("*.kt"):
        # Convert to relative path from root_dir
        relative_path = file_path.relative_to(root_path)
        
        # Count lines in the file
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                line_count = sum(1 for line in f)
        except Exception as e:
            print(f"Warning: Could not read {file_path}: {e}")
            line_count = 0
        
        kotlin_files.append({
            'path': str(relative_path),
            'lines': line_count,
            'full_path': file_path
        })
    
    return sorted(kotlin_files, key=lambda x: x['path'])

def group_files_by_directory(files):
    """Group files by their parent directory."""
    groups = {}
    
    for file_info in files:
        file_path = file_info['path']
        path_parts = Path(file_path).parts
        if len(path_parts) > 1:
            directory = str(Path(*path_parts[:-1]))
            filename = path_parts[-1]
            
            if directory not in groups:
                groups[directory] = []
            groups[directory].append({
                'name': filename,
                'lines': file_info['lines']
            })
        else:
            # Files in root directory
            if "root" not in groups:
                groups["root"] = []
            groups["root"].append({
                'name': file_path,
                'lines': file_info['lines']
            })
    
    return groups

def generate_markdown_document(files, output_file):
    """Generate a markdown document with all Kotlin files."""
    
    # Group files by directory
    grouped_files = group_files_by_directory(files)
    
    # Calculate total lines
    total_lines = sum(file_info['lines'] for file_info in files)
    
    # Sort files by line count for largest files section
    largest_files = sorted(files, key=lambda x: x['lines'], reverse=True)
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("# Kotlin Files in Project\n\n")
        f.write(f"**Total Files:** {len(files)}\n")
        f.write(f"**Total Lines:** {total_lines:,}\n\n")
        
        # Write largest files section
        f.write("## Largest Files (Top 20)\n\n")
        f.write("| Rank | File | Lines |\n")
        f.write("|------|------|-------|\n")
        
        for i, file_info in enumerate(largest_files[:20], 1):
            f.write(f"| {i} | `{file_info['path']}` | {file_info['lines']:,} |\n")
        
        f.write("\n")
        
        # Write summary by directory
        f.write("## Directory Summary\n\n")
        f.write("| Directory | File Count | Total Lines | Avg Lines/File |\n")
        f.write("|-----------|------------|-------------|----------------|\n")
        
        for directory in sorted(grouped_files.keys()):
            count = len(grouped_files[directory])
            total_dir_lines = sum(file_info['lines'] for file_info in grouped_files[directory])
            avg_lines = total_dir_lines // count if count > 0 else 0
            f.write(f"| `{directory}` | {count} | {total_dir_lines:,} | {avg_lines:,} |\n")
        
        f.write("\n")
        
        # Write detailed file list by directory
        f.write("## Detailed File List\n\n")
        
        for directory in sorted(grouped_files.keys()):
            f.write(f"### {directory}\n\n")
            
            # Sort files in directory by line count (descending)
            sorted_files = sorted(grouped_files[directory], key=lambda x: x['lines'], reverse=True)
            
            for file_info in sorted_files:
                if directory == "root":
                    f.write(f"- `{file_info['name']}` ({file_info['lines']:,} lines)\n")
                else:
                    f.write(f"- `{file_info['name']}` ({file_info['lines']:,} lines)\n")
            
            f.write("\n")
        
        # Write complete file list
        f.write("## Complete File List (Sorted by Line Count)\n\n")
        f.write("| Rank | File | Lines |\n")
        f.write("|------|------|-------|\n")
        
        for i, file_info in enumerate(largest_files, 1):
            f.write(f"| {i} | `{file_info['path']}` | {file_info['lines']:,} |\n")

def main():
    """Main function."""
    # Default to current directory if no argument provided
    root_directory = sys.argv[1] if len(sys.argv) > 1 else "app/src/main/java"
    
    if not os.path.exists(root_directory):
        print(f"Error: Directory '{root_directory}' does not exist.")
        print("Usage: python generate_kotlin_list.py [directory_path]")
        sys.exit(1)
    
    print(f"Scanning directory: {root_directory}")
    
    # Find all Kotlin files
    kotlin_files = find_kotlin_files(root_directory)
    
    print(f"Found {len(kotlin_files)} Kotlin files")
    
    # Calculate total lines
    total_lines = sum(file_info['lines'] for file_info in kotlin_files)
    print(f"Total lines of code: {total_lines:,}")
    
    # Generate the markdown document
    output_file = "KOTLIN_FILES_LIST.md"
    generate_markdown_document(kotlin_files, output_file)
    
    print(f"Generated document: {output_file}")
    
    # Print largest files as preview
    print("\nLargest 10 files:")
    largest_files = sorted(kotlin_files, key=lambda x: x['lines'], reverse=True)
    for i, file_info in enumerate(largest_files[:10]):
        print(f"  {i+1}. {file_info['path']} ({file_info['lines']:,} lines)")
    
    if len(kotlin_files) > 10:
        print(f"  ... and {len(kotlin_files) - 10} more files")

if __name__ == "__main__":
    main()
