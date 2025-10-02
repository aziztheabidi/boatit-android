#!/usr/bin/env python3
"""
Script to fix common typos throughout the codebase.
This addresses the unprofessional spelling errors in the contractor's work.
"""

import os
import re
from pathlib import Path

def fix_typos_in_file(file_path):
    """Fix typos in a single file."""
    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
        
        original_content = content
        
        # Fix common typos
        replacements = {
            'dashbaord': 'dashboard',
            'availablity': 'availability', 
            'Requset': 'Request',
            'requset': 'request',
            'getingData': 'gettingData',  # Fix the getingData typo we found earlier
            'BusinessDockTYpe': 'BusinessDockType',  # Fix capitalization typo
            'expandeds': 'isShoreExpanded', 
            'expandedi': 'isIslandExpanded',
            'BDetail': 'businessDetail',  
        }
        
        for typo, correction in replacements.items():
            content = content.replace(typo, correction)
        
        # Only write if changes were made
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        
        return False
        
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def preview_directory_renames(root_dir):
    """Preview what directories will be renamed without actually doing it."""
    print("PREVIEW: Directories that will be renamed:")
    print("-" * 60)
    
    dirs_to_rename = []
    
    for root, dirs, files in os.walk(root_dir):
        for dir_name in dirs:
            old_path = os.path.join(root, dir_name)
            
            # Check for typos and create new name
            new_name = dir_name
            if 'dashbaord' in dir_name:
                new_name = new_name.replace('dashbaord', 'dashboard')
            if 'availablity' in dir_name:
                new_name = new_name.replace('availablity', 'availability')
            
            # Only add if name actually changed
            if new_name != dir_name:
                new_path = os.path.join(root, new_name)
                dirs_to_rename.append((old_path, new_path))
                print(f"  DIR: {old_path}")
                print(f"       -> {new_path}")
                print()
    
    if not dirs_to_rename:
        print("  No directories with typos found!")
    
    print(f"\nTotal directories to rename: {len(dirs_to_rename)}")
    return dirs_to_rename

def rename_directories(root_dir):
    """Rename directories with typos."""
    renamed_dirs = []
    
    # Collect all directories to rename first (to avoid modifying while iterating)
    dirs_to_rename = []
    
    for root, dirs, files in os.walk(root_dir, topdown=False):  # Bottom-up to rename deepest first
        for dir_name in dirs:
            old_path = os.path.join(root, dir_name)
            
            # Check for typos and create new name
            new_name = dir_name
            if 'dashbaord' in dir_name:
                new_name = new_name.replace('dashbaord', 'dashboard')
            if 'availablity' in dir_name:
                new_name = new_name.replace('availablity', 'availability')
            
            # Only add if name actually changed
            if new_name != dir_name:
                new_path = os.path.join(root, new_name)
                dirs_to_rename.append((old_path, new_path))
    
    # Now perform the renames
    for old_path, new_path in dirs_to_rename:
        try:
            if os.path.exists(old_path) and not os.path.exists(new_path):
                os.rename(old_path, new_path)
                renamed_dirs.append((old_path, new_path))
                print(f"SUCCESS: Renamed directory: {old_path} -> {new_path}")
            elif os.path.exists(new_path):
                print(f"WARNING: Directory already exists: {new_path}")
            else:
                print(f"ERROR: Directory not found: {old_path}")
        except Exception as e:
            print(f"ERROR: Error renaming directory {old_path}: {e}")
    
    return renamed_dirs

def fix_typos_in_codebase(root_dir):
    """Fix typos throughout the entire codebase."""
    print("Starting typo correction process...")
    print("=" * 50)
    
    # First, rename directories
    print("Renaming directories with typos...")
    renamed_dirs = rename_directories(root_dir)
    
    # Then fix files
    print("\nFixing typos in files...")
    fixed_files = []
    total_files = 0
    
    for root, dirs, files in os.walk(root_dir):
        for file_name in files:
            if file_name.endswith('.kt') or file_name.endswith('.md'):
                file_path = os.path.join(root, file_name)
                total_files += 1
                
                if fix_typos_in_file(file_path):
                    fixed_files.append(file_path)
                    print(f"FIXED: {file_path}")
    
    print("\n" + "=" * 50)
    print("SUMMARY:")
    print(f"Total files processed: {total_files}")
    print(f"Files with typos fixed: {len(fixed_files)}")
    print(f"Directories renamed: {len(renamed_dirs)}")
    
    if fixed_files:
        print("\nFiles that were modified:")
        for file_path in fixed_files:
            print(f"  - {file_path}")
    
    if renamed_dirs:
        print("\nDirectories that were renamed:")
        for old_path, new_path in renamed_dirs:
            print(f"  - {old_path} -> {new_path}")
    
    print("\nIMPORTANT NEXT STEPS:")
    print("1. Run 'git add .' to stage all changes")
    print("2. Commit with message: 'Fix spelling errors throughout codebase'")
    print("3. Update any IDE project files if needed")
    print("4. Test the application to ensure nothing broke")
    print("5. Consider contacting contractors about quality issues")

def main():
    """Main function."""
    root_directory = "app/src/main/java"
    
    if not os.path.exists(root_directory):
        print(f"Error: Directory '{root_directory}' does not exist.")
        return
    
    print("TYPO CORRECTION SCRIPT")
    print("=" * 50)
    print("This script will fix the unprofessional spelling errors")
    print("found throughout the contractor's codebase.")
    print("=" * 50)
    
    # First show preview
    print("\nSTEP 1: Preview changes...")
    dirs_to_rename = preview_directory_renames(root_directory)
    
    if not dirs_to_rename:
        print("\nNo directory renames needed!")
    else:
        print(f"\nWARNING: {len(dirs_to_rename)} directories will be renamed!")
        print("This will affect:")
        print("  - File paths in imports")
        print("  - Package declarations")
        print("  - IDE project files")
        print("  - Git history")
    
    response = input("\nProceed with typo correction? (y/N): ")
    if response.lower() != 'y':
        print("Operation cancelled.")
        return
    
    fix_typos_in_codebase(root_directory)

if __name__ == "__main__":
    main()
